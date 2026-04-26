package com.aishwarya.FinBank.ruleengine.evaluator;

import com.aishwarya.FinBank.model.LoanApplication;


import com.aishwarya.FinBank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.model.RuleResult;
import com.aishwarya.FinBank.ruleengine.model.RuleType;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.RuleEvaluation;
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
                Condition condition = (Condition) rule.getExpression();
                RuleResult result = evaluateExpression(application,condition);
            }
            else if(rule.getType() == RuleType.COMPOSITE){
               RuleEvaluation compositeRuleEvaluationObject = compositeRuleEvaluationFactory.buildEvaluation(rule.getExpression());
               RuleResult result = compositeRuleEvaluationObject.evaluate(application);
            }
        }
        return false;
    }

    public RuleResult evaluateExpression(LoanApplication application, Condition condition){
        RuleEvaluation simpleRuleEvaluationObject = factory.createSimpleRule(condition.getField(), condition.getOperator().toString(), condition.getValue());
        RuleResult result =  simpleRuleEvaluationObject.evaluate(application);
        return result;
    }
}
