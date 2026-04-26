package com.aishwarya.FinBank.ruleengine.loader;

import com.aishwarya.FinBank.ruleengine.dto.RuleDto;
import com.aishwarya.FinBank.ruleengine.model.Rule;
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

    @Autowired
    private RuleMapper ruleMapper;

    @Override
    public List<Rule> loadRules() {
       try {
           List<RuleDto> ruleDtoList = objectMapper.readValue(new File("src/main/resources/static-rules.json"),new TypeReference<List<RuleDto>>() {});
           List<Rule> rules = ruleDtoList.stream().map(ruleMapper::toRule).toList();
           return rules;
       }
       catch (IOException e){
          throw new RuntimeException("Failed to load rules!");
       }

    }
}
