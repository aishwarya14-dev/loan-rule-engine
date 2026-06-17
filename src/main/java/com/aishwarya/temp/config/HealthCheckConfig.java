package com.aishwarya.Finbank.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

public class HealthCheckConfig {

    @Value("${spring.data.redis.url:NOT_FOUND}")
    private String redisUrl;

    @Value("${spring.datasource.url:NOT_FOUND}")
    private String dbUrl;

    @PostConstruct
    public void init() {
        System.out.println("REDIS URL = " + redisUrl);
        System.out.println("DATABASE URL = " + System.getenv("DATABASE_URL = ") + dbUrl);
        System.out.println("JWT = " + System.getenv("JWT_SECRET"));

        SecretKey key = Jwts.SIG.HS256.key().build();
        String secret = Encoders.BASE64.encode(key.getEncoded());

        System.out.println(secret);

    }
}
