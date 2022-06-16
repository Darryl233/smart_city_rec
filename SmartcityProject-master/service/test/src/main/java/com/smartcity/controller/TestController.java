package com.smartcity.controller;

import com.smartcity.common.commonutils.BlockChainController;
import com.smartcity.common.commonutils.entity.BlockChainResource;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

//@RestController
//@CrossOrigin
//@Api(description = "test")
public class TestController {

    @GetMapping("/test")
    public void sendMessage1() {

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        System.out.println(dateString);

        BlockChainResource resource = new BlockChainResource("0", "20", "null", "test_1", dateString, "Admin_1");
        String s = BlockChainController.setBlockChainResource(resource);
        System.out.println(s);
    }
}
