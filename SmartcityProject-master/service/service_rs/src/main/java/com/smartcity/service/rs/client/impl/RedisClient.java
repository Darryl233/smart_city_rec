package com.smartcity.service.rs.client.impl;

import com.smartcity.service.rs.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;


import java.util.Set;
import java.util.Vector;

public class RedisClient {
    private Jedis myJedis;
    private static final String url = "192.168.0.101";
    private static final int port = 6379;
    private static RedisClient redisClient = new RedisClient(RedisClient.url, RedisClient.port);

    private RedisClient(String url, int port) {
        this.myJedis = new Jedis(url, port);
    }

    public static RedisClient getInstance() {
        return new RedisClient(RedisClient.url, RedisClient.port);
    }

    public HashMap<String, Vector<Double>> loadEmbeddings() {
        HashMap<String, Vector<Double>> res = new HashMap<>();
        System.out.println("start load embedding");
        long cnt = 0;
        Set<String> keys = myJedis.keys("*");
        System.out.println("embedding num: " + keys.size());
        for (String key : keys) {
            String strEmbedding = myJedis.get(key);
            strEmbedding = strEmbedding.substring(1, strEmbedding.length() - 1);
            String[] temp = strEmbedding.split(", ");
            int dim = temp.length;
            Vector<Double> embd = new Vector<>(dim);
            for(int i = 0; i < dim; i++) {
                embd.add(i, Double.valueOf(temp[i]));
            }
            res.put(key, embd);
            cnt += 1;
            if (cnt % 1000 == 0) {
                System.out.println("load " + Long.toString(cnt) + " embeddings finished");
            }
        }

        return res;
    }

    public double[] getEmbedding(String key) {
        if (myJedis.exists(key)) {
            String strEmbedding = myJedis.get(key);
            return this.stringToList(strEmbedding);
        }
        return null;
    }

    private double[] stringToList(String strEmbedding) {
        strEmbedding = strEmbedding.substring(1, strEmbedding.length() - 1);
        String[] temp = strEmbedding.split(", ");
        int dim = temp.length;
        double[] embd = new double[dim];
        for(int i = 0; i < dim; i++) {
            embd[i] = Double.parseDouble(temp[i]);
        }
        return embd;
    }
}
