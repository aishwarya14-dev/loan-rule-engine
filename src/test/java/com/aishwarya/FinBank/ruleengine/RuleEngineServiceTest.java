package com.aishwarya.FinBank.service.ruleengine;

import com.aishwarya.FinBank.ruleengine.evaluator.DynamicRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.loader.RuleLoader;
import com.aishwarya.FinBank.service.RuleEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RuleEngineServiceTest {

    @InjectMocks
    private RuleEngineService ruleEngineService;

    @Mock
    private StaticRulesEvaluator staticRulesEvaluator;

    @Mock
    private DynamicRulesEvaluator dynamicRulesEvaluator;

//    @Mock
//    private RuleLoader s

    @Test
    public void testEvaluateLoanApplicationAgainstModeSelection(){

    }
}
