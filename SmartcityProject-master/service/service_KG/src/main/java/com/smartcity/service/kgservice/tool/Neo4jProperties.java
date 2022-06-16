package com.smartcity.service.kgservice.tool;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @authorE Zilig Guan
 * @authorC 关哲林
 * @date 2021/12/16 16:06
 **/
@Data
@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "neo4j")
public class Neo4jProperties {
    @Value("${uri}")
    private String uri;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;
}
