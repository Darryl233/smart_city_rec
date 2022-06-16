package com.smartcity.entity;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Cat implements InitializingBean, DisposableBean {

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean");
    }

    public void init(){
        System.out.println("InitMethod");
    }

    public void destory(){
        System.out.println("destroyMethod");
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public Cat getCat(){
        return new Cat();
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("PostConstruct");
    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("PreDestroy");
    }
}
