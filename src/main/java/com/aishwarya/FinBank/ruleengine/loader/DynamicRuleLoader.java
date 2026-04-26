package com.aishwarya.FinBank.ruleengine.loader;
import com.aishwarya.FinBank.ruleengine.model.DslRule;
import com.aishwarya.FinBank.ruleengine.model.RulePOJO;
import com.aishwarya.FinBank.ruleengine.parser.DslRulesParser;
import com.aishwarya.FinBank.ruleengine.repository.RuleRepository;
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
    public List<RulePOJO> loadRules() {
        List<RulePOJO> rulePOJOS = new ArrayList<>();
        try {
            List<DslRule> entities = repository.findAll();
            for(DslRule dslRule : entities) {
                try {
                    RulePOJO parsedRulePOJO = parser.parseDslRule(dslRule.getDslRule());
                    rulePOJOS.add(parsedRulePOJO);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rulePOJOS;
    }
}
