//package com.smartcity.resources_crud.client.Impl;
//
//import com.smartcity.common.commonutils.entity.CommonHistory;
//import com.smartcity.resources_crud.client.UserHistoryClient;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class UserHistoryClientImpl implements UserHistoryClient {
//
//    private static final String root_url = "http://127.0.0.1:8150/ucenter/userhistory/saveHistory";
//
//    @PostMapping(value = "/ucenter/userhistory/saveHistory")
//    @Override
//    public boolean saveHistory(CommonHistory history) {
//        String url = root_url + user;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<MultiValueMap<String, String>> request;
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        for (String item: itemList) {
//            params.add("items", item);
//        }
//        request = new HttpEntity<>(params, headers);
//
//        //System.out.println(headers);
//        //System.out.println(request);
//
//        //step 2. post http call
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
//        return response.getBody();
//    }
//}
