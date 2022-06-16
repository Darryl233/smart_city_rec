package com.smartcity.common.commonutils;

import com.smartcity.common.commonutils.entity.BlockChainResource;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class BlockChainController {
    /*public String setBlockChainUser(BlockChainUser blockChainUser){
    }*/

    public static String setBlockChainResource(BlockChainResource resource){
        // step 1. request
        String url = "http://192.168.8.197:8000/api/v1/uploadResource";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("Cost", resource.getCost());
        map.add("GetScore", resource.getGetScore());
        map.add("Hash", resource.getHash());
        map.add("Id", resource.getId());
        map.add("Time", resource.getTime());
        map.add("Uploader", resource.getUploader());

        HttpEntity<MultiValueMap<String, String>> request;
        request = new HttpEntity<>(map, headers);

        System.out.println(request.getBody());

        //step 2. post http call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
        return response.getBody();
    }
}
