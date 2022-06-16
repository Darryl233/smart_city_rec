package com.smartcity.service.rs.client.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartcity.service.rs.client.MCRecClient;
import io.swagger.models.auth.In;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

@Component
public class MCRecClientImpl implements MCRecClient {
    private static final String root_url = "http://192.168.0.101:8181/test/";

    public ArrayList<String> getRecommendation(String user, ArrayList<String> itemList, Integer type) {
        // step 1. request
        String url = root_url + user;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> request;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (String item: itemList) {
            params.add("items", item);
        }
        request = new HttpEntity<>(params, headers);

        //System.out.println(headers);
        //System.out.println(request);

        //step 2. post http call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call failed");
        String rsp = response.getBody();
        JSONObject jsonObj = JSON.parseObject(rsp);
        JSONArray recRsp =  jsonObj.getJSONArray(Integer.toString(type));
        ArrayList<String> res = new ArrayList<>();
        if (recRsp == null)
            return res;
        for(int i = 0; i < recRsp.size(); i++){
            JSONArray itemTuple = recRsp.getJSONArray(i);
            res.add(itemTuple.getString(0));
        }
        return res;
    }
}
