package com.smartcity.client.Impl;

import com.smartcity.client.MCRecClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//@Component
public class MCRecClientImpl implements MCRecClient {
    public String getRecommendation(String user) {
        String url = "http://192.168.8.103:8181/test/" + user;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request;
        request = new HttpEntity<>(headers);

        //System.out.println(headers);
        //System.out.println(request);

        //step 2. post http call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
        return response.getBody();
    }
}
