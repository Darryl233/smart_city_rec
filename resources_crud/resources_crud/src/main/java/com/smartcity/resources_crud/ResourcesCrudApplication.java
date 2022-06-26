package com.smartcity.resources_crud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(scanBasePackages = {"com.smartcity.resources_crud.client"})
@MapperScan("com.smartcity.resources_crud.mapper")
@ComponentScan(basePackages = {"com.smartcity.resources_crud.client", "com.smartcity.resources_crud.*", "com.smartcity.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class ResourcesCrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourcesCrudApplication.class, args);
    }

}
