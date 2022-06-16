package com.smartcity.service.domainservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.smartcity.service.domainservice.mapper")
public class DomainConfig {
}
