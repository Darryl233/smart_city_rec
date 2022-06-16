package com.smartcity.service.solutionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.smartcity")//为了扫描swagger
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.smartcity.service.solutionservice.mapper")
public class SolutionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolutionApplication.class, args);
    }
}
