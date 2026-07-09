package com.aishwarya.Finbank.ruleengine.evaluator;
import com.aishwarya.Finbank.enums.RuleType;
import com.aishwarya.Finbank.exceptions.RuleEvaluationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.service.FactorEvaluationResultService;
import com.aishwarya.Finbank.service.LoanApplicationResultService;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.Finbank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.Finbank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.Finbank.service.RuleResultService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Qualifier("staticRulesEvaluator")
public class StaticRulesEvaluator implements RulesEvaluator<LoanApplicationResult> {

    private final SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    private final CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    private final RuleResultService ruleResultService;

    private final LoanApplicationResultService loanApplicationResultService;

    private final FactorEvaluationResultService factorEvaluationResultService;

    private final RuleEngineMetrics metrics;


    @Override
    public LoanApplicationResult evaluateRules(LoanApplication application, List<Rule> rules) {
        List<RuleResult> ruleResultList = new ArrayList<>();
        for (Rule rule : rules) {
            RuleResult ruleResult = null;
            try{
                if (rule.getType() == null || rule.getType() == RuleType.SIMPLE) {
                    ruleResult = evaluateExpression(application, rule);
                } else if (rule.getType() == RuleType.COMPOSITE) {
                    Expression expression = rule.getExpression();
                    RuleEvaluation compositeRuleEvaluationObject = compositeRuleEvaluationFactory.buildCompositeRuleEvaluationObject();
                    ruleResult = compositeRuleEvaluationObject.evaluate(application,rule);
                }
                if (ruleResult != null) {
                    ruleResultService.saveRuleResult(ruleResult);
                    ruleResultList.add(ruleResult);
                }
            }
            catch (RuleEvaluationException e){
                log.error("Failed to evaluate rule : {} for application id: {} - {}",
                        rule.getExpression(), application.getId(), e.getMessage(), e);
                metrics.incrementEvaluationSkipped();
            }
        }
        return loanApplicationResultService.calculateAndSaveLoanApplicationResult(ruleResultList,application,false);
    }

    public RuleResult evaluateExpression(LoanApplication application, Rule rule) {
        RuleEvaluation simpleRuleEvaluationObject = simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject();
        metrics.incrementEvaluationTotal();
        return simpleRuleEvaluationObject.evaluate(application,rule);
    }
}
