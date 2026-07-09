package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.enums.Action;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.model.expression.AndExpression;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.model.value.DoubleValue;
import com.aishwarya.Finbank.model.value.IntValue;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.utility.RuleMessageGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompositeRuleEvaluationTest {

    @InjectMocks
    private CompositeRuleEvaluation compositeRuleEvaluation;
    @Mock
    private LoanFieldAccessorRegistry loanFieldAccessorRegistry;

    @Mock
    private RuleMessageGenerator ruleMessageGenerator;

    @Mock
    private LoanTypeFactorConfigService loanTypeFactorConfigService;

    @Mock
    private Rule rule;

    @Mock
    private RuleEngineMetrics metrics;

    private LoanApplication application;

    @Mock
    private RuleEvaluationHelper ruleEvaluationHelper;

    @BeforeEach
    void setup(){
        application = mock(LoanApplication.class);
    }

    @Test
    void shouldPassForAndLogicWhenAllRulesPass() {
        LoanType loanType = mock(LoanType.class);
        Condition salaryCondition = new Condition(
                "monthlyIncome",
                Operator.GTE,
                new DoubleValue(50000)
        );
        Condition ageCondition = new Condition(
                "age",
                Operator.GT,
                new IntValue(21)
        );
        Expression expression =
                new AndExpression(
                        salaryCondition,
                        ageCondition
                );

        LoanTypeFactorConfig loanTypeFactorConfig = new LoanTypeFactorConfig();

        when(rule.getExpression()).thenReturn(expression);
        when(application.getLoanType()).thenReturn(loanType);
        when(loanType.getId()).thenReturn(1L);
        when(rule.getFactorId()).thenReturn(2L);

        when(loanTypeFactorConfigService.getLoanTypeFactorConfig(any(), any()))
                .thenReturn(loanTypeFactorConfig);

        when(ruleEvaluationHelper.getActualValGetterFunction("monthlyIncome")).thenReturn(app -> 60000);
        when(ruleEvaluationHelper.getActualValue(any(), eq(application), eq("monthlyIncome")))
                .thenReturn(60000);
        when(ruleEvaluationHelper.compareActualVsExpectedValue(60000, salaryCondition))
                .thenReturn(true);

        when(ruleEvaluationHelper.getActualValGetterFunction("age"))
                .thenReturn(app -> 25);
        when(ruleEvaluationHelper.getActualValue(any(), eq(application), eq("age")))
                .thenReturn(25);
        when(ruleEvaluationHelper.compareActualVsExpectedValue(25, ageCondition))
                .thenReturn(true);

        when(ruleEvaluationHelper.calculateRuleContributionScore(rule,true)).thenReturn(20.0);
        RuleResult result = compositeRuleEvaluation.evaluate(application, rule);

        assertTrue(result.isPassed());
        assertEquals(20.0, result.getRuleEvaluationScore());
    }

}
