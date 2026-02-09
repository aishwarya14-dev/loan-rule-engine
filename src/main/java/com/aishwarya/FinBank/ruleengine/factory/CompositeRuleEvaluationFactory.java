package com.aishwarya.FinBank.ruleengine.factory;


import com.aishwarya.FinBank.ruleengine.model.Logic;
import com.aishwarya.FinBank.ruleengine.model.condition.CompositeCondition;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.CompositeRuleEvaluation;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.SimpleRuleEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompositeRuleEvaluationFactory {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private SimpleRuleEvaluationFactory factory;


    public RuleEvaluation createCompositeRule(Logic logic, List<Condition> conditions) {
        CompositeRuleEvaluation compositeRuleEvaluation = context.getBean(CompositeRuleEvaluation.class);
        compositeRuleEvaluation.setLogic(logic.toString());

        List<RuleEvaluation> ruleEvaluations = new ArrayList<>();
        for (Condition condition : conditions) {
            if (condition instanceof CompositeCondition) {
                CompositeCondition compositeCondition = (CompositeCondition) condition;
                List<Condition> subConditions = compositeCondition.getConditions();
                RuleEvaluation subCompositeRuleEvaluation = createCompositeRule(compositeCondition.getLogic(), subConditions);
                ruleEvaluations.add(subCompositeRuleEvaluation);
            } else {
                SimpleRuleEvaluation simpleRuleEvaluation = (SimpleRuleEvaluation) factory.createSimpleRule(
                        ((com.aishwarya.FinBank.ruleengine.model.condition.SimpleCondition) condition).getField(),
                        ((com.aishwarya.FinBank.ruleengine.model.condition.SimpleCondition) condition).getOperator(),
                        ((com.aishwarya.FinBank.ruleengine.model.condition.SimpleCondition) condition).getValue()
                );
                ruleEvaluations.add(simpleRuleEvaluation);
            }
        }

        compositeRuleEvaluation.setRuleEvaluations(ruleEvaluations);
        return compositeRuleEvaluation;
    }
}
