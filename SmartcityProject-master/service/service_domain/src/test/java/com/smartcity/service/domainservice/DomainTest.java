package com.smartcity.service.domainservice;

import com.smartcity.service.domainservice.entity.Domain;
import com.smartcity.service.domainservice.mapper.DomainMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DomainTest {


    @Autowired
    private DomainMapper domainMapper;

    @Test
    void contextLoads() {
        Domain achievements = domainMapper.selectOne(null);
        System.out.println(achievements);
    }

}