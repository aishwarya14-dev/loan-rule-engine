package com.aishwarya.Finbank.ruleengine.evaluator;
import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.model.Action;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.model.RuleResult;
import com.aishwarya.Finbank.model.RuleType;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.value.IntValue;
import com.aishwarya.Finbank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.Finbank.ruleengine.factory.SimpleRuleEvaluationFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.aishwarya.Finbank.ruleengine.evaluator.StaticRulesEvaluator;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class StaticRulesEvaluatorTest {

    @InjectMocks
    private StaticRulesEvaluator staticRulesEvaluator;

    @Mock
    private SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    @Mock
    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    @Test
    void testShouldEvaluateSimpleRulesSuccessfully() {
        Condition condition = new Condition(
                        "creditScore",
                        Operator.GT,
                        new IntValue(700)
                );
        Rule rule = new Rule(
                        condition,
                        Action.APPROVE,
                        RuleType.SIMPLE
                );
        RuleResult ruleResult = mock(RuleResult.class);
    }
}
