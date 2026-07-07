package com.aishwarya.Finbank.ruleengine.factory;

import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.utility.RuleMessageGenerator;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.Finbank.ruleengine.evaluation.SimpleRuleEvaluation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleRuleEvaluationFactory {

    private final LoanFieldAccessorRegistry registry;
    private final RuleMessageGenerator ruleMessageGenerator;
    private final LoanTypeFactorConfigService loanTypeFactorConfigService;
    private final RuleEngineMetrics metrics;

    public RuleEvaluation buildSimpleRuleEvaluationObject(Rule rule) {
        return new SimpleRuleEvaluation(rule, registry, ruleMessageGenerator,loanTypeFactorConfigService,metrics);
    }
}
