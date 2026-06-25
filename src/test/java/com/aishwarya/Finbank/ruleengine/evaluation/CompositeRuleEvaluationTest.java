package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompositeRuleEvaluationTest {

    @InjectMocks
    private CompositeRuleEvaluation compositeRuleEvaluation;

    @Mock
    private RuleMessageGenerator ruleMessageGenerator;

    @Mock
    private RuleEvaluation ruleEvaluation1;

    @Mock
    private RuleEvaluation ruleEvaluation2;

    @Mock
    private Rule rule;

    private LoanApplication application;

    @BeforeEach
    void setup(){
        application = mock(LoanApplication.class);
    }

    @Test
    void shouldPassForAndLogicWhenAllRulesPass() {

        RuleResult result1 =
                new RuleResult(true, "salary matched", 10.0, application);

        RuleResult result2 =
                new RuleResult(true, "age matched", 10.0, application);

        when(ruleEvaluation1.evaluate(application)).thenReturn(result1);
        when(ruleEvaluation2.evaluate(application)).thenReturn(result2);

        when(rule.getAction()).thenReturn(Action.APPROVE);
        when(rule.getEvidenceWeight()).thenReturn(20.0);

        when(ruleMessageGenerator.generateMessage(anyString(), eq(true)))
                .thenReturn("Composite passed");

        CompositeRuleEvaluation evaluation =
                new CompositeRuleEvaluation(
                        List.of(ruleEvaluation1, ruleEvaluation2),
                        Logic.AND,
                        ruleMessageGenerator,
                        rule);

        RuleResult result = evaluation.evaluate(application);

        assertTrue(result.isPassed());
        assertEquals(20.0, result.getRuleEvaluationScore());
    }

}
