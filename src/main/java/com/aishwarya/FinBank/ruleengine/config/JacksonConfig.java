package com.aishwarya.FinBank.ruleengine.config;

import com.aishwarya.FinBank.ruleengine.model.value.RuleValue;
import com.aishwarya.FinBank.ruleengine.model.value.RuleValueDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RuleValue.class, new RuleValueDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
