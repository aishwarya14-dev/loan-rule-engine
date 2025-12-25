package com.aishwarya.FinBank.ruleengine.loader;

import com.aishwarya.FinBank.model.DslRule;
import com.aishwarya.FinBank.repository.RuleRepository;
import com.aishwarya.FinBank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("dslRuleLoader")
public class DynamicRuleLoader implements RuleLoader {

    @Autowired
    private RuleRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    @Autowired
    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    @Override
    public List<Rule> loadRules() {
        List<DslRule> entities = repository.findAll();
        List<Rule> rules = new ArrayList<>();

        for (DslRule entity : entities) {
            try {
                Rule rule = new Rule(entity.getCondition(), entity.getAction(), entity.getLogic(), entity.getType());
                rules.add(rule);
            } catch (Exception e) {
            }
        }
        return rules;
    }
}
