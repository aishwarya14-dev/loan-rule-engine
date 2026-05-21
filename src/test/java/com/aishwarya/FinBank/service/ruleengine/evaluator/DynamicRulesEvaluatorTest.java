package com.aishwarya.FinBank.service.ruleengine.evaluator;

import com.aishwarya.FinBank.ruleengine.evaluator.DynamicRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.FinBank.ruleengine.factory.SimpleRuleEvaluationFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DynamicRulesEvaluatorTest {

    @Mock
    private SimpleRuleEvaluationFactory simpleRuleEvaluationFactory;

    @Mock
    private CompositeRuleEvaluationFactory compositeRuleEvaluationFactory;

    @InjectMocks
    private DynamicRulesEvaluator dynamicRulesEvaluator;

    @Test
    public void testEvaluateRules(){

    }
}
