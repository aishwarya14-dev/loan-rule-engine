package com.aishwarya.Finbank.ruleengine.evaluator;
import com.aishwarya.Finbank.enums.RuleSeverity;
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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Primary
public class DynamicRulesEvaluator implements RulesEvaluator {

    private final SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    private final CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    private final RuleResultService ruleResultService;

    private final LoanApplicationResultService loanApplicationResultService;

    private final FactorEvaluationResultService factorEvaluationResultService;

    private final RuleEngineMetrics metrics;


    @Override
    public void evaluateRules(LoanApplication application, List<Rule> rules) {
        List<RuleResult> ruleResultList = new ArrayList<>();
        for (Rule rule : rules) {
            RuleResult ruleResult = null;
            try{
                if (rule.getType() == null || rule.getType() == RuleType.SIMPLE) {
                    ruleResult = evaluateExpression(application, rule);
                } else if (rule.getType() == RuleType.COMPOSITE) {
                    Expression expression = rule.getExpression();
                    RuleEvaluation compositeRuleEvaluationObject = compositeRuleEvaluationFactory.buildCompositeRuleEvaluationObject(expression,rule);
                    ruleResult = compositeRuleEvaluationObject.evaluate(application);
                }

                if(ruleResult != null){
                    ruleResultService.saveRuleResult(ruleResult);
                    ruleResultList.add(ruleResult);
                }

                // Log the evaluation result for each rule
                logRuleEvaluation(rule, ruleResult, application);

                // Check for hard rejection and break the loop if triggered
                if (isHardRejectionApplicable(ruleResult, rule)) {
                    ruleResult.setMessage("Hard rejection triggered due to rule: " + rule.getExpression());
                    ruleResult.setHardReject(true);
                    log.info("Hard rejection triggered for application id: {} due to rule: {}",
                            application.getId(), rule.getExpression());
                    break; // Stop evaluating further rules if hard rejection is triggered
                }

            }
            catch (RuntimeException e){
                log.error("Failed to evaluate rule : {} for application id: {} - {}",
                        rule.getExpression(), application.getId(), e.getMessage(), e);
                metrics.incrementEvaluationSkipped();
            }
        }
        loanApplicationResultService.calculateAndSaveLoanApplicationResult(ruleResultList,application,true);
    }

    private RuleResult evaluateExpression(LoanApplication application, Rule rule) {
        RuleEvaluation simpleRuleEvaluationObject = simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject(rule);
        metrics.incrementEvaluationTotal();
        return simpleRuleEvaluationObject.evaluate(application);
    }

    private void logRuleEvaluation(Rule rule, RuleResult ruleResult, LoanApplication application) {
        log.info("Rule evaluated: {} for application id: {}. Result: {}, Score: {}, Message: {}",
                rule.getExpression(), application.getId(), ruleResult.isPassed(),
                ruleResult.getRuleEvaluationScore(), ruleResult.getMessage());
    }

    private boolean isHardRejectionApplicable(RuleResult ruleResult, Rule rule) {
        if (ruleResult.isPassed() && rule.getSeverity() == RuleSeverity.HARD_REJECT) {
            return true;
        }
        return false;
    }
}
