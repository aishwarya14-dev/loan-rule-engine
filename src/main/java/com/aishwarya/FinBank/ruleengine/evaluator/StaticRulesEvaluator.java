package com.aishwarya.FinBank.ruleengine.evaluator;

import com.aishwarya.FinBank.model.*;
import com.aishwarya.FinBank.model.expression.Condition;
import com.aishwarya.FinBank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.FinBank.service.RuleResultService;
import com.aishwarya.FinBank.utility.LoanFieldAccessorRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Qualifier("dynamicRulesEvaluator")
public class StaticRulesEvaluator implements RulesEvaluator {

    private SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    private RuleResultService ruleResultService;


    @Override
    public boolean evaluateRules(LoanApplication application, List<Rule> rules) {
        for (Rule rule : rules) {
            if(rule.getType() == null || rule.getType() == RuleType.SIMPLE){
                Condition condition = (Condition) rule.getExpression();
                evaluateExpression(application,condition);
            }
            else if(rule.getType() == RuleType.COMPOSITE){
               RuleEvaluation compositeRuleEvaluationObject = compositeRuleEvaluationFactory.buildCompositeRuleEvaluationObject(rule.getExpression());
               RuleResult result = compositeRuleEvaluationObject.evaluate(application);
                ruleResultService.saveRuleResult(result);
            }
        }
        return false;
    }

    public void evaluateExpression(LoanApplication application, Condition condition){
        RuleEvaluation simpleRuleEvaluationObject = simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject(condition.getField(), condition.getOperator(), condition.getValue());
        RuleResult result =  simpleRuleEvaluationObject.evaluate(application);
        ruleResultService.saveRuleResult(result);
    }

}
