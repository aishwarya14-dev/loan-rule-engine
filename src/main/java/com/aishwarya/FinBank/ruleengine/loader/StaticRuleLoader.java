package com.aishwarya.FinBank.ruleengine.loader;

import com.aishwarya.FinBank.dto.rules.staticrules.RuleDto;
import com.aishwarya.FinBank.mapper.RuleMapper;
import com.aishwarya.FinBank.model.LoanType;
import com.aishwarya.FinBank.model.Rule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class StaticRuleLoader implements RuleLoader{

    private ObjectMapper objectMapper;

    private RuleMapper ruleMapper;

    @Override
    public List<Rule> loadRules(LoanType loanType) {
       try {
           List<RuleDto> ruleDtoList = objectMapper.readValue(
                   getClass().getClassLoader().getResourceAsStream("static-rules.json"),
                   new TypeReference<List<RuleDto>>() {}
           );
           List<Rule> rules = ruleDtoList.stream().map(ruleMapper::toRule).toList();
           return rules;
       }
       catch (IOException e){
           System.err.println("Error loading rules: " + e.getMessage());
           throw new RuntimeException("Failed to load rules: " + e.getMessage(), e);
       } catch (Exception e) {
           throw new RuntimeException("Unexpected error while loading rules: " + e.getMessage(), e);
       }
    }
}
