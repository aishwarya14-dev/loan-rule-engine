package com.aishwarya.FinBank.config;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//@Component
//public class HealthCheckConfig {
//
//    @Value("${spring.data.redis.url:NOT_FOUND}")
//    private String redisUrl;
//
//    @Value("${spring.datasource.url:NOT_FOUND}")
//    private String dbUrl;
//
//    @PostConstruct
//    public void init(){
//        System.out.println("REDIS URL = " + redisUrl);
//        System.out.println("DATABASE URL = " + dbUrl);
//        System.out.println("JWT = " + System.getenv("JWT_SECRET"));
//    }
//}
