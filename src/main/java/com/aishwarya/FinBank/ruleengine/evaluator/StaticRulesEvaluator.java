package com.aishwarya.FinBank.ruleengine.evaluator;

import com.aishwarya.FinBank.model.LoanApplication;


import com.aishwarya.FinBank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.model.RuleResult;
import com.aishwarya.FinBank.ruleengine.model.RuleType;
import com.aishwarya.FinBank.ruleengine.model.condition.CompositeCondition;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.model.condition.SimpleCondition;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.RuleEvaluation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("staticRuleEvaluator")
public class StaticRulesEvaluator implements RulesEvaluator {

    @Autowired
    private SimpleRuleEvaluationFactory factory;
    @Autowired
    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;


    @Override
    public boolean evaluateRules(LoanApplication application, List<Rule> rules) {
        for (Rule rule : rules) {
            if(rule.getType() == null || rule.getType() == RuleType.SIMPLE){
                SimpleCondition condition = (SimpleCondition) rule.getCondition();
                return evaluateCondition(application, condition);
            }
            else if(rule.getType() == RuleType.COMPOSITE){
                CompositeCondition compositeCondition = (CompositeCondition) rule.getCondition();
                List<Condition> conditions = compositeCondition.getConditions();
                RuleEvaluation compositeRuleEvaluationObject = compositeRuleEvaluationFactory.createCompositeRule(compositeCondition.getLogic(), conditions);
                RuleResult result = compositeRuleEvaluationObject.evaluate(application);
            }
        }
        return false;
    }

    public boolean evaluateCondition(LoanApplication application, SimpleCondition condition){
        RuleEvaluation simpleRuleEvaluationObject = factory.createSimpleRule(condition.getField(), condition.getOperator(), condition.getValue());
        RuleResult result =  simpleRuleEvaluationObject.evaluate(application);
        return result.isPassed();
    }
}
