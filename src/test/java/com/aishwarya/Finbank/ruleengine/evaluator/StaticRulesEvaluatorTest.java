package com.aishwarya.Finbank.ruleengine.evaluator;
import com.aishwarya.Finbank.enums.RuleType;
import com.aishwarya.Finbank.exceptions.RuleEvaluationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
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

@ExtendWith(MockitoExtension.class)
public class StaticRulesEvaluatorTest {

    @InjectMocks
    private StaticRulesEvaluator staticRulesEvaluator;

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
        when(simpleRuleEvaluationFactory.buildSimpleRuleEvaluationObject()).thenReturn(simpleRuleEvaluationObject);
        when(simpleRuleEvaluationObject.evaluate(application,rule)).thenReturn(ruleResult);

         staticRulesEvaluator.evaluateRules(
                        application,
                        List.of(rule)
                );

        //to check that the code actually called a method on the mock
        verify(ruleResultService).saveRuleResult(ruleResult);

        verify(loanApplicationResultService)
                .calculateAndSaveLoanApplicationResult(
                        List.of(ruleResult),
                        application,
                        false
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

      when(compositeRuleEvaluationFactory.buildCompositeRuleEvaluationObject()).thenReturn(compositeRuleEvaluationObject);
      when(compositeRuleEvaluationObject.evaluate(loanApplication,rule)).thenReturn(ruleResult);

        staticRulesEvaluator.evaluateRules(
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
        RuleEvaluation compositeRuleEvaluationObject = mock(RuleEvaluation.class);

        RuleResult validResult = new RuleResult();

        when(invalidRule.getType()).thenReturn(RuleType.COMPOSITE);
        when(validRule.getType()).thenReturn(RuleType.COMPOSITE);
        when(compositeRuleEvaluationFactory.buildCompositeRuleEvaluationObject()).thenReturn(compositeRuleEvaluationObject);
        when(compositeRuleEvaluationObject.evaluate(application,invalidRule)).thenThrow(new RuleEvaluationException("failed to evaluate rule"));
        when(compositeRuleEvaluationObject.evaluate(application,validRule)).thenReturn(validResult);

        staticRulesEvaluator.evaluateRules(application,List.of(invalidRule,validRule));

        verify(ruleResultService).saveRuleResult(validResult);

        verify(loanApplicationResultService)
                .calculateAndSaveLoanApplicationResult(
                        List.of(validResult),
                        application,
                        false
                );

    }
}
