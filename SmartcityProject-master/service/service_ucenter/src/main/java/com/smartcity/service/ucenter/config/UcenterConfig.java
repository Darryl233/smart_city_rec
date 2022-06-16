package com.smartcity.service.ucenter.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.smartcity.service.ucenter.mapper")
public class UcenterConfig {
}
