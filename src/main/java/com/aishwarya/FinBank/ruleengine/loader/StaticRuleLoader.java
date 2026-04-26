package com.aishwarya.FinBank.ruleengine.loader;

import com.aishwarya.FinBank.ruleengine.model.RulePOJO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component("staticRuleLoader")
public class StaticRuleLoader implements RuleLoader{
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public List<RulePOJO> loadRules() {
       try {
           List<RulePOJO> rulePOJOS = objectMapper.readValue(new File("src/main/resources/static-rules.json"),new TypeReference<List<RulePOJO>>() {});
           return rulePOJOS;
       }
       catch (IOException e){
          throw new RuntimeException("Failed to load rules!");
       }

    }
}
