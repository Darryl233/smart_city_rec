package com.smartcity.service.paperservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.smartcity.service.paperservice.mapper")
public class PaperConfig {
}
