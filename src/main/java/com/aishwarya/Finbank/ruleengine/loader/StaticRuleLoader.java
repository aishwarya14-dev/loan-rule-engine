package com.aishwarya.Finbank.ruleengine.loader;

import com.aishwarya.Finbank.dto.rules.staticrules.StaticRule;
import com.aishwarya.Finbank.mapper.RuleMapper;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.model.Rule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Qualifier("staticRuleLoader")
public class StaticRuleLoader implements RuleLoader {

    private ObjectMapper objectMapper;

    private RuleMapper ruleMapper;

    @Override
    public List<Rule> loadRules(LoanType loanType) {
        try {
            List<StaticRule> staticRuleList = objectMapper.readValue(
                    getClass().getClassLoader().getResourceAsStream("static-rules.json"),
                    new TypeReference<List<StaticRule>>() {
                    }
            );
            List<Rule> rules = staticRuleList.stream().map(ruleMapper::toRule).toList();
            return rules;
        } catch (IOException e) {
            log.error("Failed to load static rules from JSON file", e);
            throw new RuntimeException("Failed to load rules: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while loading static rules", e);
            throw new RuntimeException("Unexpected error while loading rules: " + e.getMessage(), e);
        }
    }
}
