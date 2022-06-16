package com.smartcity.service.rs.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.smartcity.service.rs.mapper")
public class RSConfig {
}
