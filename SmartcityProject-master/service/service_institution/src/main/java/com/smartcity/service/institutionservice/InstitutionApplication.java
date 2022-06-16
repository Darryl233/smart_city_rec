package com.smartcity.service.institutionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.smartcity")//为了扫描swagger
@EnableDiscoveryClient
@EnableFeignClients
public class InstitutionApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstitutionApplication.class, args);
    }
}
