package com.aishwarya.Finbank.ruleengine.factory;

import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.model.RuleMessageGenerator;
import com.aishwarya.Finbank.model.value.RuleValue;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.Finbank.ruleengine.evaluation.SimpleRuleEvaluation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleRuleEvaluationFactory {

    private LoanFieldAccessorRegistry registry;

    private RuleMessageGenerator ruleMessageGenerator;

    public RuleEvaluation buildSimpleRuleEvaluationObject(String field, Operator operator, RuleValue value) {
        return new SimpleRuleEvaluation(field, operator, value, registry, ruleMessageGenerator);
    }
}
