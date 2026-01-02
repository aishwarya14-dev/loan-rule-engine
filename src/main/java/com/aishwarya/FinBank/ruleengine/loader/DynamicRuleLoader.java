package com.aishwarya.FinBank.ruleengine.loader;
import com.aishwarya.FinBank.repository.RuleRepository;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.parser.DslRulesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("dslRuleLoader")
public class DynamicRuleLoader implements RuleLoader {

    @Autowired
    private RuleRepository repository;

    @Autowired
    private DslRulesParser parser;

    @Override
    public List<Rule> loadRules() {
        List<Rule> rules = new ArrayList<>();
        try {
            List<String> entities = repository.findAll();
            for(String dslRule : entities) {
                try {
                    Rule parsedRule = parser.parseDslRule(dslRule);
                    rules.add(parsedRule);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rules;
    }
}
