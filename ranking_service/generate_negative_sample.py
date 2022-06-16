import json

from utils import redis_client
from sql_handler import SQLHandler
import numpy as np


def get_sim(u, v):
    return np.dot(u, v) / ((np.dot(u, u) ** 0.5) * (np.dot(v, v) ** 0.5))


def generate_negative_samples():
    path = './data/negative_ui_knn_500'
    ui = {}
    histories = SQLHandler.load_user_history()
    print(histories)
    for history in histories:
        if history.log_id not in ui.keys():
            ui[history.log_id] = []
        ui[history.log_id].append(history.kg_id.replace("unit", "institution"))

    res, id2id = SQLHandler.load_entities()
    print(ui.keys())
    with open(path, "w") as file:
        for user, item in ui.items():
            scores = []
            user_embedding = json.loads(redis_client.get(user))
            for kg_id in res:
                if kg_id != item:
                    try:
                        v = json.loads(redis_client.get(kg_id))
                    except:
                        print(kg_id)
                        continue
                    scores.append([kg_id, get_sim(user_embedding, v)])
            scores.sort(key=lambda x: x[1], reverse=True)
            limit = max(len(scores), 500)
            scores = scores[:limit]
            for score in scores:
                file.write(str(user) + "\t" + str(score[0]))
                file.write("\n")



if __name__ == "__main__":
    generate_negative_samples()
