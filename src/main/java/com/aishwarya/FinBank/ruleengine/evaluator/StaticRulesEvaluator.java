package com.aishwarya.FinBank.ruleengine.evaluator;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.loader.StaticRuleLoader;
import com.aishwarya.FinBank.ruleengine.model.Condition;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("staticRuleEvaluator")
public class StaticRulesEvaluator implements RuleEvaluator{

    private StaticRuleLoader ruleLoader;
    @Autowired
    private @Qualifier("compositeRuleCondition") RuleEvaluation compositeRuleCondition;

    @Autowired
    private @Qualifier("simpleRuleCondition") RuleEvaluation simpleRuleCondition;
    @Autowired
    private SimpleRuleEvaluationFactory factory;
    @Autowired
    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    @Override
    public void evaluate(LoanApplication application,List<Rule> rules) {
        for (Rule rule : rules) {
            if(rule.getType() == null || rule.getType().equals("SIMPLE")){
                Condition condition = rule.getCondition();
                evaluateCondition(application, condition);
            }
            else if(rule.getType().equals("COMPOSITE")){
//                List<Object> conditions = rule.getCondition().getConditions();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode ruleNode = mapper.valueToTree(rule);
                RuleEvaluation compositeRuleEvaluationObject = compositeRuleEvaluationFactory.fromJson(ruleNode);
                compositeRuleEvaluationObject.evaluate(application);
            }
        }
    }

    public void evaluateCondition(LoanApplication application,Condition condition){
        RuleEvaluation simpleRuleEvaluationObject = factory.createSimpleCondition(condition.getField(), condition.getOperator(), condition.getValue());
        boolean result = simpleRuleEvaluationObject.evaluate(application);
    }
}
