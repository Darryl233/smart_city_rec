import time

import numpy as np
from utils import redis_client, normalize
from sql_handler import SQLHandler
import json


# todo: (实体，关系，实体）三元组中关系id是relation_id or 8种关系如：专家提出专利算一种）
def load_rating_file_as_list(filename):
    ratingList = []
    with open(filename, 'r') as f:
        for line in f.readlines():
            line = line.rstrip("\n")
            arr = line.split('\t')
            tmp_list = [arr[0], arr[1], 1]
            ratingList.append(tmp_list)
    return ratingList


# 读取正例和反例
def load_negative_file(filename):
    negativeDict = dict()
    with open(filename, 'r') as f:
        for line in f.readlines():
            line = line.rstrip("\n")
            a = line.split("\t")
            if a[0] not in negativeDict.keys():
                negativeDict[a[0]] = []
            negativeDict[a[0]].append(a[1])
    positives = SQLHandler.load_user_history()
    for positive in positives:
        if positive.log_id not in negativeDict.keys():
            negativeDict[positive.log_id] = []
        negativeDict[positive.log_id].append(positive.kg_id.replace("unit", "institution"))
    return negativeDict


def load_path_as_map(filename):
    print("load uiii_metapath")
    path_dict = {}
    path_num = 0
    ctn_node_in_uiii = 4
    with open(filename, 'r') as infile:
        for line in infile.readlines():
            line = line.rstrip("\n")
            arr = line.strip().split('\t')
            u, i = arr[0].split(',')
            path_dict[(u, i)] = []
            path_num = max(int(arr[1]), path_num)
            for path in arr[2:]:
                tmp = path.split(' ')[0].split('-')
                node_list = []
                for node in tmp:
                    node_list.append(node)
                path_dict[(u, i)].append(node_list)
    return path_dict, path_num, ctn_node_in_uiii


def get_sim(u, r, i):
    return np.sum(np.square(u + r - i))
    # return np.dot(u, v) / ((np.dot(u, u) ** 0.5) * (np.dot(v, v) ** 0.5))


class Dataset(object):
    def __init__(self):
        self.outfile_path = "./data/smartcity.uiii_5_1_test"
        self.num_users, self.num_items = 0, 0
        self.user2id, self.item2id = {}, {}
        self.id2user, self.id2item = [], []
        self.ui_dict = {}
        self.ii_dict = {}

        # 分别对 self.num_users、self.user_embedding 和 self.num_items、self.item_embedding 进行 初始化和赋值
        self.embedding_size = 64
        self.user_embedding, self.item_embedding = None, None

        r = redis_client
        self.load_embedding(r)
        self.load_path(r)
        r.close()

        self.metapath_based_randomwalk()

        self.testNegatives = load_negative_file('./data/negative_ui_knn_500')

        self.path_uiii, self.uiii_path_num, self.ctn_node_in_uiii = \
            load_path_as_map('./data/smartcity.uiii_5_1_test')

    def load_embedding(self, r):
        self.num_users = 0
        self.num_items = 0

        for _key in r.keys():
            if str(_key)[0] == "1" and len(str(_key)) > 15:
                self.num_users += 1
            else:
                self.num_items += 1

        self.user_embedding = np.zeros((self.num_users, 64))
        self.item_embedding = np.zeros((self.num_items, 64))

        for _key in r.keys():
            if str(_key)[0] == "1" and len(str(_key)) > 15:
                if _key not in self.user2id.keys():
                    self.id2user.append(_key)
                    self.user2id[_key] = len(self.id2user) - 1
                    self.user_embedding[self.user2id[_key]] = json.loads(r.get(_key))
            else:
                if _key not in self.item2id.keys():
                    self.id2item.append(_key)
                    self.item2id[_key] = len(self.id2item) - 1
                    self.item_embedding[self.item2id[_key]] = json.loads(r.get(_key))

        print(self.item_embedding[self.item2id['history']])

    def load_path(self, r):
        self.ui_dict, self.ii_dict = SQLHandler.load_path()

    def metapath_based_randomwalk(self):
        pair_list = []
        for u in self.id2user:
            for i in self.id2item:
                pair_list.append([u, i])
        print('load pairs finished num = ', len(pair_list))
        ctn = 0
        t1 = time.time()
        avg = 0
        with open(self.outfile_path, "w") as outfile:
            for u, m in pair_list:
                ctn += 1
                # print u, m
                if ctn % 10000 == 0:
                    print('%d [%.4f]\n' % (ctn, time.time() - t1))
                ii_list = self.walk_uiii(u, m)
                if ii_list:
                    outfile.write(str(u) + ',' + str(m) + '\t' + str(len(ii_list)))
                    for ii in ii_list:
                        path = ['u' + str(u), str(ii[0]), str(ii[1]), str(m)]
                        outfile.write('\t' + '-'.join(path) + ' ' + str(ii[2]))
                    outfile.write('\n')

    def walk_uiii(self, s_u, e_i):
        limit = 10
        item1_list = []
        if s_u not in self.ui_dict.keys():
            return
        min_sim = 999999
        max_sim = -1
        for item1 in self.ui_dict[s_u]:
            sim = get_sim(self.user_embedding[self.user2id[s_u]], self.item_embedding[self.item2id['history']],
                          self.item_embedding[self.item2id[item1]])
            if sim > max_sim:
                max_sim = sim
            if sim < min_sim:
                min_sim = sim
        for item1 in self.ui_dict[s_u]:
            sim = get_sim(self.user_embedding[self.user2id[s_u]], self.item_embedding[self.item2id['history']],
                          self.item_embedding[self.item2id[item1]])
            item1_list.append([item1, normalize(max_sim, min_sim, sim)])

        item1_list.sort(key=lambda x: x[1], reverse=True)
        item1_list = item1_list[:limit]

        item2_list = []
        for item in self.ii_dict.get(e_i, []):
            item2_list.append([item, 1])

        ii_list = []
        for item1 in item1_list:
            for item3 in item2_list:
                item1_id = item1[0]
                item2_id = item3[0]
                if item1_id in self.ii_dict and item2_id in self.ii_dict[item1_id] and item1_id != e_i:
                    sim = item1[1]
                    # if sim > 0.0:
                    ii_list.append([item1_id, item2_id, sim])
        ii_list.sort(key=lambda x: x[2], reverse=True)
        ii_list = ii_list[:5]

        if len(ii_list) == 0:
            return
        return ii_list


if __name__ == '__main__':
    dataset = Dataset()
    print(dataset.user_embedding)
    print(dataset.item_embedding)
    print(dataset.testNegatives)
    print(dataset.path_uiii)
    print(dataset.uiii_path_num)
    print(dataset.user_embedding)
