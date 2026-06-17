package com.aishwarya.Finbank.ruleengine.evaluator;

import com.aishwarya.Finbank.ruleengine.evaluator.DynamicRulesEvaluator;
import com.aishwarya.Finbank.ruleengine.factory.CompositeRuleEvaluationFactory;
import com.aishwarya.Finbank.ruleengine.factory.SimpleRuleEvaluationFactory;
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
