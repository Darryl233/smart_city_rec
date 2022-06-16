package com.smartcity.service.achievementservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.smartcity")//为了扫描swagger
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.smartcity.service.achievementservice.mapper")
public class AchievementApplication {
    public static void main(String[] args) {
        SpringApplication.run(AchievementApplication.class, args);
    }
}
