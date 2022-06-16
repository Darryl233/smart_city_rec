package com.smartcity.service.requirementservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.smartcity.service.requirementservice.mapper")
public class RequirementConfig {
}
