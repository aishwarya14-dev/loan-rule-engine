package com.aishwarya.Finbank.ruleengine.evaluator;



import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.model.RuleResult;
import com.aishwarya.Finbank.model.RuleType;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.Finbank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.Finbank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.Finbank.service.RuleResultService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.aishwarya.Finbank.model.LoanApplication;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Primary
public class DynamicRulesEvaluator implements RulesEvaluator {

    private SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    private RuleResultService ruleResultService;


    @Override
    public boolean evaluateRules(LoanApplication application, List<Rule> rules) {
        for (Rule rule : rules) {
            if (rule.getType() == null || rule.getType() == RuleType.SIMPLE) {
                Condition condition = (Condition) rule.getExpression();
                evaluateExpression(application, condition);
            } else if (rule.getType() == RuleType.COMPOSITE) {
                RuleEvaluation compositeRuleEvaluationObject = compositeRuleEvaluationFactory.buildCompositeRuleEvaluationObject(rule.getExpression());
                RuleResult result = compositeRuleEvaluationObject.evaluate(application);
                ruleResultService.saveRuleResult(result);
            }
        }
        return false;
    }

    private void evaluateExpression(LoanApplication application, Condition condition) {
        RuleEvaluation simpleRuleEvaluationObject = simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject(condition.getField(), condition.getOperator(), condition.getValue());
        RuleResult result = simpleRuleEvaluationObject.evaluate(application);
        ruleResultService.saveRuleResult(result);
    }
}
