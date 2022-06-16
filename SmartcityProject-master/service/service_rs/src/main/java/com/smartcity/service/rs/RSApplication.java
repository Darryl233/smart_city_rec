package com.smartcity.service.rs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = "com.smartcity")  // 为了扫描swagger
@EnableScheduling   // 开启定时任务的注解
public class RSApplication {
    public static void main(String[] args) {
        SpringApplication.run(RSApplication.class, args);
    }
}
