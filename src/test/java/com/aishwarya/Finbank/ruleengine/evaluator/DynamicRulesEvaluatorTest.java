package com.aishwarya.Finbank.ruleengine.evaluator;

import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.model.RuleResult;
import com.aishwarya.Finbank.enums.RuleType;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.Finbank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.Finbank.ruleengine.factory.SimpleRuleEvaluationFactory;
import com.aishwarya.Finbank.service.LoanApplicationResultService;
import com.aishwarya.Finbank.service.RuleResultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DynamicRulesEvaluatorTest {

    @InjectMocks
    private DynamicRulesEvaluator dynamicRulesEvaluator;

    @Mock
    private SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    @Mock
    private RuleResultService ruleResultService;

    @Mock
    private LoanApplicationResultService loanApplicationResultService;

    @Mock
    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    @Mock
    private RuleEngineMetrics metrics;

    @Test
    void testShouldEvaluateSimpleRulesSuccessfully() {
        LoanApplication application = mock(LoanApplication.class);
        Rule rule = mock(Rule.class);
        RuleResult ruleResult = new RuleResult();
        RuleEvaluation simpleRuleEvaluationObject = mock(RuleEvaluation.class);

        when(rule.getType()).thenReturn(RuleType.SIMPLE);
        when(simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject(rule)).thenReturn(simpleRuleEvaluationObject);
        when(simpleRuleEvaluationObject.evaluate(application)).thenReturn(ruleResult);

         dynamicRulesEvaluator.evaluateRules(
                application,
                List.of(rule)
        );

        //to check that the code actually called a method on the mock
        verify(ruleResultService).saveRuleResult(ruleResult);

        verify(loanApplicationResultService)
                .calculateAndSaveLoanApplicationResult(
                        List.of(ruleResult),
                        application,
                        true
                );

    }

    @Test
    void testShouldEvaluateCompositeRulesSuccessfully() {
        LoanApplication loanApplication = mock(LoanApplication.class);
        Rule rule = mock(Rule.class);
        Expression expression = mock(Expression.class);
        RuleEvaluation compositeRuleEvaluationObject = mock(RuleEvaluation.class);
        RuleResult ruleResult = new RuleResult();

        when(rule.getType()).thenReturn(RuleType.COMPOSITE);
        when(rule.getExpression()).thenReturn(expression);

        when(compositeRuleEvaluationFactory.buildCompositeRuleEvaluationObject(expression,rule)).thenReturn(compositeRuleEvaluationObject);
        when(compositeRuleEvaluationObject.evaluate(loanApplication)).thenReturn(ruleResult);

        dynamicRulesEvaluator.evaluateRules(
                loanApplication,
                List.of(rule)
        );
        verify(ruleResultService).saveRuleResult(ruleResult);
    }

    @Test
    void shouldSkipFailedRuleAndContinueEvaluatingRemainingRules() {
        LoanApplication application = mock(LoanApplication.class);

        Rule invalidRule = mock(Rule.class);
        Rule validRule = mock(Rule.class);

        RuleResult validResult = new RuleResult();

        when(invalidRule.getType()).thenReturn(RuleType.SIMPLE);
        when(validRule.getType()).thenReturn(RuleType.SIMPLE);

        RuleEvaluation ruleEvaluation1 = mock(RuleEvaluation.class);
        RuleEvaluation ruleEvaluation2 = mock(RuleEvaluation.class);

        when(simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject(invalidRule)).thenReturn(ruleEvaluation1);
        when(simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject(validRule)).thenReturn(ruleEvaluation2);

        when(ruleEvaluation1.evaluate(application))
                .thenThrow(new RuntimeException("evaluation exception"));

        when(ruleEvaluation2.evaluate(application)).thenReturn(validResult);

        dynamicRulesEvaluator.evaluateRules(application,List.of(invalidRule,validRule));

        verify(ruleResultService).saveRuleResult(validResult);

        verify(loanApplicationResultService)
                .calculateAndSaveLoanApplicationResult(
                        List.of(validResult),
                        application,
                        true
                );

    }
}
