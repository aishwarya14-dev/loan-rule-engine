package com.aishwarya.Finbank.ruleengine.evaluation;
import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.enums.Action;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.value.DoubleValue;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.utility.RuleMessageGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimpleRuleEvaluationTest {

   @InjectMocks
   private SimpleRuleEvaluation simpleRuleEvaluation;

   @Mock
   private RuleMessageGenerator ruleMessageGenerator;

   @Mock
   private LoanTypeFactorConfigService loanTypeFactorConfigService;

   @Mock
   private RuleEngineMetrics metrics;

   @Mock
   private RuleEvaluationHelper ruleEvaluationHelper;

   @Test
   void shouldEvaluateApproveRuleSuccessfully() {
      Rule rule = mock(Rule.class);
      LoanApplication application = mock(LoanApplication.class);
      Condition condition = new Condition();
      condition.setField("salary");
      condition.setOperator(Operator.GT);
      condition.setValue(new DoubleValue(50000));
      LoanTypeFactorConfig loanTypeFactorConfig = mock(LoanTypeFactorConfig.class);

      LoanType loanType = mock(LoanType.class);
      when(application.getLoanType()).thenReturn(loanType);
      when(loanType.getId()).thenReturn(1L);
      when(rule.getFactorId()).thenReturn(2L);

      when(rule.getExpression()).thenReturn(condition);

      when(ruleEvaluationHelper.getActualValGetterFunction("salary")).thenReturn(app -> 60000);
      when(ruleEvaluationHelper.getActualValue(any(), eq(application), eq("salary")))
              .thenReturn(60000);
      when(ruleEvaluationHelper.compareActualVsExpectedValue(60000, condition))
              .thenReturn(true);
      when(ruleMessageGenerator.generateMessage(
              any(), any(), any(), any(), anyBoolean()))
              .thenReturn("Passed");
      when(loanTypeFactorConfigService.getLoanTypeFactorConfig(any(),any())).thenReturn(loanTypeFactorConfig);
      when(ruleEvaluationHelper.calculateRuleContributionScore(rule, true))
              .thenReturn(10.0);


      RuleResult result = simpleRuleEvaluation.evaluate(application,rule);
      assertTrue(result.isPassed());
      assertEquals(10.0, result.getRuleEvaluationScore());
   }
}
