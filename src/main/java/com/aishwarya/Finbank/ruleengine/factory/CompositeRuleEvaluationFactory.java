package com.aishwarya.Finbank.ruleengine.factory;


import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluationHelper;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.enums.Logic;
import com.aishwarya.Finbank.utility.RuleMessageGenerator;
import com.aishwarya.Finbank.model.expression.AndExpression;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.model.expression.OrExpression;
import com.aishwarya.Finbank.ruleengine.evaluation.CompositeRuleEvaluation;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.Finbank.ruleengine.evaluation.SimpleRuleEvaluation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CompositeRuleEvaluationFactory {

    private final LoanFieldAccessorRegistry registry;
    private final RuleMessageGenerator ruleMessageGenerator;
    private final LoanTypeFactorConfigService loanTypeFactorConfigService;
    private final RuleEngineMetrics metrics;
    private final RuleEvaluationHelper ruleEvaluationHelper;

    public RuleEvaluation buildCompositeRuleEvaluationObject() {
        return new CompositeRuleEvaluation(
                ruleMessageGenerator,
                loanTypeFactorConfigService,
                metrics,
                ruleEvaluationHelper
        );
    }
}
