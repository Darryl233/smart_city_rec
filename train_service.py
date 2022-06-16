import os
import random

import numpy as np
import simplejson
from keras import backend
from keras.models import Model
from keras.layers import Lambda, Conv1D, GlobalMaxPooling1D, Dropout
from keras.layers import Input, Dense, Reshape, concatenate, multiply
from keras.regularizers import l2
from keras.optimizers import Adagrad, Adam, SGD, RMSprop, Nadam
import tensorflow.compat.v1 as tf

import generate_negative_sample
from c3_Dataset_Smartcity_redis import Dataset
# from Dataset import Dataset
import math
from app_base import app
import time
import argparse
from transE import TransE

from sql_handler import SQLHandler

tf.enable_eager_execution()
config = tf.ConfigProto()
config.gpu_options.allow_growth = True
session = tf.Session(config=config)
tf.disable_v2_behavior()
os.environ["CUDA_VISIBLE_DEVICES"] = "1"
# app = Flask(__name__, template_folder="web")
topK = 11

entities2id = {}
relations2id = {}

def load_data():
    global relations2id, entities2id
    entity, entities2id = SQLHandler.load_entities()

    relation, triple_list, relations2id = SQLHandler.load_relations()
    users_info = SQLHandler.load_user_info()
    for user in users_info:
        entity.append(user)
        entities2id[user] = user
    relation_history_id = "rhistory"
    relation_history_type = "history"
    relation.append(relation_history_type)
    relations2id[relation_history_type] = relation_history_id
    user_histories = SQLHandler.load_user_history()
    for history in user_histories:
        triple_list.append([history.log_id, relation_history_type, history.kg_id.replace("unit", "institution")])

    return entity, relation, triple_list


def norm_l1(h, r, t):
    return np.sum(np.fabs(h + r - t))


def norm_l2(h, r, t):
    return np.sum(np.square(h + r - t))


def parse_args():
    parser = argparse.ArgumentParser(description="Run MCRec.")
    parser.add_argument('--dataset', nargs='?', default='ml-100k',
                        help='Choose a dataset.')
    parser.add_argument('--epochs', type=int, default=2,
                        help='Number of epochs.')
    parser.add_argument('--batch_size', type=int, default=100000,
                        help='Batch size.')
    parser.add_argument('--learner', nargs='?', default='adam',
                        help='Specify an optimizer: adagrad, adam, rmsprop, sgd')
    parser.add_argument('--verbose', type=int, default=1,
                        help='Show performance per X iterations')
    parser.add_argument('--lr', type=float, default=0.001,
                        help='Learning rate.')
    parser.add_argument('--latent_dim', type=int, default='128',
                        help="Embedding size for user and item embedding")
    parser.add_argument('--latent_layer_dim', nargs='?', default='[512, 256, 128, 64]',
                        help="Embedding size for each layer")
    parser.add_argument('--num_neg', type=int, default=4,
                        help='Number of negative instances to pair with a positive instance.')
    return parser.parse_args()


def get_uiii_embedding(uiii_input, uiii_path_nums, ctn_node_in_uiii, embed_size):
    path_input = Lambda(slice_1, output_shape=(ctn_node_in_uiii, embed_size), arguments={'index': 0})(uiii_input)

    conv_uiii = Conv1D(filters=128,
                       kernel_size=4,
                       activation='relu',
                       kernel_regularizer=l2(0.0),
                       kernel_initializer='glorot_uniform',
                       padding='valid',
                       strides=1,
                       name='uiii_conv')
    output = conv_uiii(path_input)
    output = GlobalMaxPooling1D()(output)
    output = Dropout(0.5)(output)

    for i in range(1, uiii_path_nums):
        path_input = Lambda(slice_1, output_shape=(ctn_node_in_uiii, embed_size), arguments={'index': i})(uiii_input)
        tmp_output = conv_uiii(path_input)
        tmp_output = GlobalMaxPooling1D()(tmp_output)
        tmp_output = Dropout(0.5)(tmp_output)
        output = concatenate([output, tmp_output])

    output = Reshape(target_shape=(uiii_path_nums, 128))(output)
    output = GlobalMaxPooling1D()(output)
    return output


def metapath_attention(user_latent, item_latent, metapath_latent, att_size):
    path_num, mp_latent_size = metapath_latent.shape[1].value, metapath_latent.shape[2].value
    dense_layer_1 = Dense(att_size,
                          activation='relu',
                          kernel_initializer='glorot_normal',
                          kernel_regularizer=l2(0.001),
                          name='metapath_attention_layer_1')

    dense_layer_2 = Dense(1,
                          activation='relu',
                          kernel_initializer='glorot_normal',
                          kernel_regularizer=l2(0.001),
                          name='metapath_attention_layer_2')

    metapath = Lambda(slice_2, output_shape=(mp_latent_size,), arguments={'index': 0})(metapath_latent)
    inputs = concatenate([user_latent, item_latent, metapath])
    output = (dense_layer_1(inputs))
    output = (dense_layer_2(output))
    for i in range(1, path_num):
        metapath = Lambda(slice_2, output_shape=(mp_latent_size,), arguments={'index': i})(metapath_latent)
        inputs = concatenate([user_latent, item_latent, metapath])
        tmp_output = (dense_layer_1(inputs))
        tmp_output = (dense_layer_2(tmp_output))
        output = concatenate([output, tmp_output])

    atten = Lambda(lambda x: backend.softmax(x), name='metapath_attention_softmax')(output)

    output = Lambda(lambda x: backend.sum(x[0] * backend.expand_dims(x[1], -1), 1))([metapath_latent, atten])

    return output


def user_attention(user_latent, path_output):
    latent_size = user_latent.shape[1].value
    inputs = concatenate([user_latent, path_output])
    output = Dense(latent_size,
                   activation='relu',
                   kernel_initializer='glorot_normal',
                   kernel_regularizer=l2(0.001),
                   name='user_attention_layer')(inputs)
    atten = Lambda(lambda x: backend.softmax(x), name='user_attention_softmax')(output)
    # 数组对应元素位置相乘
    return multiply([user_latent, atten])


def item_attention(item_latent, path_output):
    latent_size = item_latent.shape[1].value
    inputs = concatenate([item_latent, path_output])
    output = Dense(latent_size,
                   activation='relu',
                   kernel_initializer='glorot_normal',
                   kernel_regularizer=l2(0.001),
                   name='item_attention_layer')(inputs)
    atten = Lambda(lambda x: backend.softmax(x), name='item_attention_softmax')(output)
    # 数组对应元素位置相乘
    return multiply([item_latent, atten])


def get_model(_path_nums_list, _ctn_node_in_path,
              embed_size, _layers=None, _latent_dim=128):
    if _layers is None:
        _layers = [512, 256, 128, 64]
    user_input = Input(shape=(embed_size,), dtype='float32', name='user_input', sparse=False)
    item_input = Input(shape=(embed_size,), dtype='float32', name='item_input', sparse=False)
    uiii_input = Input(shape=(_path_nums_list[0], _ctn_node_in_path[0], embed_size,),
                       dtype='float32', name='uiii_input')

    # Embedding层：将最后一维上的元素转换为长度为output_dim的稠密向量，当前latent_dim=128
    # 将 user_id 转化为 长度128的稠密向量
    Embedding_User_Feedback = Dense(_latent_dim,
                                    activation='relu',
                                    name='user_feedback_embedding')
    Embedding_Item_Feedback = Dense(_latent_dim,
                                    activation='relu',
                                    name='item_feedback_embedding')
    user_latent = Reshape(target_shape=(_latent_dim,))(Embedding_User_Feedback(user_input))
    item_latent = Reshape(target_shape=(_latent_dim,))(Embedding_Item_Feedback(item_input))

    uiii_latent = get_uiii_embedding(uiii_input, _path_nums_list[0], _ctn_node_in_path[0], embed_size)

    path_output = concatenate([uiii_latent, uiii_latent])
    path_output = Reshape(target_shape=(2, 128))(path_output)
    path_output = metapath_attention(user_latent, item_latent, path_output, att_size=128)

    user_atten = user_attention(user_latent, path_output)
    item_atten = item_attention(item_latent, path_output)

    output = concatenate([user_atten, path_output, item_atten])

    for idx in range(0, len(_layers)):
        layer = Dense(_layers[idx],
                      kernel_regularizer=l2(0.001),
                      kernel_initializer='glorot_normal',
                      activation='relu',
                      name='item_layer%d' % idx)
        output = layer(output)

    prediction_layer = Dense(1,
                             activation='sigmoid',
                             kernel_initializer='lecun_normal',
                             name='prediction')
    prediction = prediction_layer(output)

    return Model(inputs=[user_input, item_input, uiii_input],
                 outputs=[prediction])


def get_train_instances(_train, _user_embedding, _item_embedding, _path_uiii, _path_nums, _ctn_node_in_path,
                        _embed_size, _dataset):
    def data_generator():
        x1 = []
        x2 = []
        x3 = []
        y = []
        item_cnt = 0
        for u in _train.keys():
            items = _train[u]
            user_input = []
            item_input = []
            uiii_input = np.zeros(shape=(len(items), _path_nums[0], _ctn_node_in_path[0], _embed_size))
            itemlist = []
            item_cnt += len(items)

            k = 0
            for i in items:
                if i == '0':
                    continue
                user_input.append(_user_embedding[_dataset.user2id[u]])
                x1.append(np.array(_user_embedding[_dataset.user2id[u]]))
                item_input.append(_item_embedding[_dataset.item2id[i]])
                x2.append(np.array(_item_embedding[_dataset.item2id[i]]))
                itemlist.append(i)

                if (u, i) in _path_uiii:
                    ctn_node_list = -1
                    for node_list in _path_uiii[(u, i)]:
                        ctn_node_list += 1
                        for ctn_node in range(len(node_list)):
                            if ctn_node == 0:
                                uiii_input[k][ctn_node_list][ctn_node] = \
                                    _user_embedding[_dataset.user2id[u]]
                            else:
                                uiii_input[k][ctn_node_list][ctn_node] = \
                                    _item_embedding[_dataset.user2id[u]]
                k += 1
            for i in range(len(items)):
                x3.append(uiii_input[i])
        y = np.array([-1 for i in range(item_cnt)])
        return [x1, x2, x3], y

    return data_generator()


def slice_1(x, index):
    return x[:, index, :, :]


def slice_2(x, index):
    return x[:, index, :]


def prediction_model(user, _model, _dataset, _user_embedding, _item_embedding, _num_users, _num_items, _path_uiii,
                     _path_nums,
                     _ctn_node_in_path, _embed_size, _testNegatives, _K):
    """
    Evaluate the performance (Hit_Ratio, NDCG) of top-K recommendation
    Return: score of each test rating.
    """

    def prediction_one_user(u):
        if type(_testNegatives) == dict:
            items = _testNegatives[u]
        elif type(_testNegatives) == list:
            items = _testNegatives
        else:
            return []
        map_item_score = {}
        user_input = []
        item_input = []
        uiii_input = np.zeros(shape=(len(items), _path_nums[0], _ctn_node_in_path[0], _embed_size))
        itemlist = []
        map_expert_score = {}
        map_institution_score = {}
        map_requirement_score = {}
        map_solution_score = {}
        map_case_score = {}
        map_achievement_score = {}
        map_paper_score = {}
        map_patent_score = {}

        k = 0
        for i in items:
            if i == '0':
                continue
            user_input.append(_user_embedding[_dataset.user2id[u]])
            item_input.append(_item_embedding[_dataset.item2id[i]])
            itemlist.append(i)

            if (u, i) in _path_uiii:
                ctn_node_list = -1
                for node_list in _path_uiii[(u, i)]:
                    ctn_node_list += 1
                    for ctn_node in range(len(node_list)):
                        if ctn_node == 0:
                            uiii_input[k][ctn_node_list][ctn_node] = \
                                _user_embedding[_dataset.user2id[u]]
                        else:
                            uiii_input[k][ctn_node_list][ctn_node] = \
                                _item_embedding[_dataset.user2id[u]]
            k += 1
        predictions = _model.predict(
            x=[np.array(user_input), np.array(item_input), uiii_input], batch_size=256, verbose=0
        )
        for i in range(len(predictions)):
            item = itemlist[i]
            if item[:3] == "exp":
                map_expert_score[item] = np.float64(predictions[i][0])
            elif item[:3] == "uni":
                map_institution_score[item] = np.float64(predictions[i][0])
            elif item[:3] == "req":
                map_requirement_score[item] = np.float64(predictions[i][0])
            elif item[:3] == "sol":
                map_solution_score[item] = np.float64(predictions[i][0])
            elif item[:3] == "cas":
                map_case_score[item] = np.float64(predictions[i][0])
            elif item[:3] == "ach":
                map_achievement_score[item] = np.float64(predictions[i][0])
            elif item[:3] == "pap":
                map_paper_score[item] = np.float64(predictions[i][0])
            elif item[:3] == "pat":
                map_patent_score[item] = np.float64(predictions[i][0])
            else:
                map_item_score[item] = np.float64(predictions[i][0])
        # ranklist 保存 top_K 个 用户u 的推荐结果
        ranklist = dict()
        ranklist[1] = sorted(map_expert_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        ranklist[2] = sorted(map_institution_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        ranklist[3] = sorted(map_requirement_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        ranklist[4] = sorted(map_solution_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        ranklist[5] = sorted(map_case_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        ranklist[6] = sorted(map_achievement_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        ranklist[7] = sorted(map_paper_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        ranklist[8] = sorted(map_patent_score.items(), key=lambda x: x[1], reverse=True)[:_K]
        # print("user %s, predictions:" % u)
        # print(ranklist)
        # print(heapq.nlargest(_K, map_item_score, key=map_item_score.get))
        return ranklist

    user_ranklist = prediction_one_user(user)
    return user_ranklist


def train_test_split(sample_dict, train_size=0.8):
    _train = {}
    _test = {}
    for k, v in sample_dict.items():
        if k not in _train:
            _train[k] = []
            _test[k] = []
        samples_index = set(random.sample(range(len(v)), int(train_size * len(v))))
        for i in range(len(v)):
            if i in samples_index:
                _train[k].append(v[i])
            else:
                _test[k].append(v[i])

    return _train, _test


#
# @app.route('/test/<_user>', methods=['get', 'post'])
# def test(_user):
#     if _user not in testNegatives.keys():
#         return simplejson.dumps(dict())
#
#     user_ranklist = prediction_model(_user, model, dataset, user_embedding, item_embedding, num_users, num_items,
#                                      path_uiii, path_nums, ctn_node_in_path, embedding_size,
#                                      testNegatives, topK)
#     print(user_ranklist)
#     return simplejson.dumps(user_ranklist)


def start_app():
    app.jinja_env.auto_reload = True
    app.config['TEMPLATES_AUTO_RELOAD'] = True
    app.run(debug=False, host='0.0.0.0', port=8181, threaded=True)


def _train(layers, latent_dim, learning_rate, epochs, batch_size):
    while True:
        t1 = time.time()
        entity_set, relation_set, triple_list = load_data()
        transE = TransE(entity_set, relation_set, triple_list, embedding_dim=64, lr=0.05, margin=1.0, norm=2)
        transE.train()
        generate_negative_sample.generate_negative_samples()
        dataset = Dataset()
        testNegatives = dataset.testNegatives
        path_uiii = dataset.path_uiii
        user_embedding, item_embedding = dataset.user_embedding, dataset.item_embedding
        num_users, num_items = dataset.num_users, dataset.num_items
        path_nums = [dataset.uiii_path_num]
        ctn_node_in_path = [dataset.ctn_node_in_uiii]
        embedding_size = dataset.embedding_size

        train, test = train_test_split(testNegatives)

        print("Load data done [%.1f s]. #user_num=%d, #item_num=%d" %
              (time.time() - t1, num_users, num_items))
        print('path nums = ', path_nums)
        print('ctn_node_in_path = ', ctn_node_in_path)
        # layers = [512, 256, 128, 64]
        # latent_dim = 128
        model = get_model(path_nums, ctn_node_in_path, embedding_size, layers, latent_dim)
        model.compile(optimizer=Adam(lr=float(learning_rate), decay=1e-4),
                      loss='binary_crossentropy')
        x, y = get_train_instances(train, user_embedding, item_embedding, path_uiii, path_nums, ctn_node_in_path,
                                   embedding_size, dataset)

        # todo: 改为model.fit maybe
        model.fit(x, y, batch_size=int(batch_size), epochs=int(epochs))
        mj = model.to_json()
        with open("MCRec_v1.json", "w") as file:
            file.write(mj)
        model.save("MCRec_v1.h5")
        # model.load_weights("MCRec_weights_v2.h5")
        for user in testNegatives.keys():
            t2 = time.time()
            total_ranklist = prediction_model(user, model, dataset, user_embedding, item_embedding, num_users,
                                              num_items,
                                              path_uiii, path_nums, ctn_node_in_path, embedding_size,
                                              testNegatives, topK)

            print('%s 预测为： [耗时 %.1f]' % (user, time.time() - t2))
            print(total_ranklist)

            t2 = time.time()
            total_ranklist = prediction_model(user, model, dataset, user_embedding, item_embedding, num_users,
                                              num_items,
                                              path_uiii, path_nums, ctn_node_in_path, embedding_size,
                                              testNegatives, topK)

            print('%s 预测为： [耗时 %.1f]' % (user, time.time() - t2))
            print(total_ranklist)
        print(f"model train complete, timestamp: {time.time()}")
        time.sleep(3600)


if __name__ == '__main__':
    args = parse_args()
    datasource = args.dataset  # ml-100k
    latent_dim = args.latent_dim  # 128
    layers = eval(args.latent_layer_dim)  # [512, 256, 128, 64]
    learning_rate = args.lr  # 0.001
    epochs = args.epochs  # 30
    batch_size = args.batch_size  # 256
    num_negatives = args.num_neg  # 4 Number of negative instances to pair with a positive instance
    learner = args.learner  # adam
    verbose = args.verbose  # 展示每x个iteration的训练效果

    _train(layers, latent_dim, learning_rate, 2, batch_size)
