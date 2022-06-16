import numpy
import redis
import json
from utils import redis_client
import ast
# print(redis_client.get("string"))
# cnt = 0
# with open("data/negative_ui_knn_500", "r") as f:
#     for line in f:
#         cnt += 1
#         print(line)
#         if cnt > 100:
#             break
keys = redis_client.keys()
achi_map = {}
a = redis_client.get("achi_32")
a = ast.literal_eval(a)

for key in keys:
    if "achi" in key:
        score = 0.0
        embd = redis_client.get(key)
        embd = ast.literal_eval(embd)
        for i in range(64):
            score += (embd[i] - a[i]) * (embd[i] - a[i])
        achi_map[key] = score
dd = sorted(achi_map.items(), key=lambda x: x[1])
print(dd[100:150])
print(len(dd))
b = redis_client.get("achi_7163")
b = ast.literal_eval(b)
score = 0.0
for i in range(64):
    score += (b[i] - a[i]) * (b[i] - a[i])
print(score)
