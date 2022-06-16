package com.smartcity.service.expertservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.smartcity.service.expertservice.mapper")
public class ExpertConfig {
}
