package com.smartcity.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartcity.client.MCRecClient;
import com.smartcity.common.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

//@RestController
//@CrossOrigin
//@Api(description = "")
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MCRecClient mcRecClient;

    // 发送消息
    @GetMapping("/kafka/normal/{message}")
    public void sendMessage1(@PathVariable("message") String normalMessage) {
        kafkaTemplate.send("test", normalMessage);
    }

    @GetMapping("/test/{userid}")
    public R getRecommendation(@PathVariable String userid){
        String recommendation = mcRecClient.getRecommendation(userid);

        Type mapOfStringObjectType = new TypeToken<Map<Integer, List<List<Object>>>>() {}.getType();
        Gson gson = new Gson();
        Map<Integer, List<List<Object>>> obj = gson.fromJson(recommendation, mapOfStringObjectType);
        /*
        System.out.println(recommendation);
        System.out.println(obj.get("exp"));
        System.out.println(obj.get("exp").get(0));
        System.out.println(obj.get("exp").get(0).get(0));
        System.out.println(obj.get("exp").get(0).get(0).getClass());
        System.out.println(obj.get("exp").get(0).get(1));
        System.out.println(obj.get("exp").get(0).get(1).getClass());
        */
        return R.ok().data("item", obj);
    }
}

