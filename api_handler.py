import json

import simplejson
from app_base import app
from MCRec import testNegatives, prediction_model, model, dataset, user_embedding, item_embedding, num_users,\
    num_items, path_uiii, path_nums, ctn_node_in_path, embedding_size, topK
from flask import request


@app.route('/test/<_user>', methods=['post'])
def test(_user):
    req = request.get_json()
    items = req.get('items', [])

    if _user not in testNegatives.keys():
        return simplejson.dumps(dict())
    if not items:
        user_ranklist = prediction_model(_user, model, dataset, user_embedding, item_embedding, num_users, num_items,
                                         path_uiii, path_nums, ctn_node_in_path, embedding_size,
                                         testNegatives, topK)
    else:
        user_ranklist = prediction_model(_user, model, dataset, user_embedding, item_embedding, num_users, num_items,
                                         path_uiii, path_nums, ctn_node_in_path, embedding_size,
                                         items, topK)
    print(user_ranklist)
    return simplejson.dumps(user_ranklist)


if __name__ == '__main__':

    app.run(debug=False, host='0.0.0.0', port=8181, threaded=False)
