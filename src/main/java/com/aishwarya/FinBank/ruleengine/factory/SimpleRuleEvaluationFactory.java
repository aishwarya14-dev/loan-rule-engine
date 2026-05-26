package com.aishwarya.FinBank.ruleengine.factory;


import com.aishwarya.FinBank.model.RuleMessageGenerator;
import com.aishwarya.FinBank.model.value.RuleValue;
import com.aishwarya.FinBank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.evaluation.SimpleRuleEvaluation;
import com.aishwarya.FinBank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.FinBank.utility.Operator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleRuleEvaluationFactory {

    private LoanFieldAccessorRegistry registry;

    private RuleMessageGenerator ruleMessageGenerator;

    public RuleEvaluation buildSimpleRuleEvaluationObject(String field, Operator operator, RuleValue value) {
        return new SimpleRuleEvaluation(field, operator, value, registry,ruleMessageGenerator);
    }
}
