import redis

redis_client = redis.Redis(host='localhost', port=6379, decode_responses=True)


def normalize(_max, _min, score):
    res = 1 - (score - _min) / (_max - _min)
    return res
