package com.aishwarya.Finbank.ruleengine.factory;


import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.model.Logic;
import com.aishwarya.Finbank.model.RuleMessageGenerator;
import com.aishwarya.Finbank.model.expression.AndExpression;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.model.expression.OrExpression;
import com.aishwarya.Finbank.ruleengine.evaluation.CompositeRuleEvaluation;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.Finbank.ruleengine.evaluation.SimpleRuleEvaluation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CompositeRuleEvaluationFactory {

    private LoanFieldAccessorRegistry registry;

    private RuleMessageGenerator ruleMessageGenerator;

    public RuleEvaluation buildCompositeRuleEvaluationObject(Expression expression) {
        if (expression instanceof Condition condition) {
            return new SimpleRuleEvaluation(
                    condition.getField(),
                    condition.getOperator(),
                    condition.getValue(),
                    registry,
                    ruleMessageGenerator
            );

        } else if (expression instanceof AndExpression andExpr) {
            List<RuleEvaluation> evaluations = List.of(
                    buildCompositeRuleEvaluationObject(andExpr.getLeft()),
                    buildCompositeRuleEvaluationObject(andExpr.getRight())
            );
            return new CompositeRuleEvaluation(evaluations, Logic.AND, ruleMessageGenerator);

        } else if (expression instanceof OrExpression orExpr) {
            List<RuleEvaluation> evaluations = List.of(
                    buildCompositeRuleEvaluationObject(orExpr.getLeft()),
                    buildCompositeRuleEvaluationObject(orExpr.getRight())
            );
            return new CompositeRuleEvaluation(evaluations, Logic.OR, ruleMessageGenerator);
        }

        throw new IllegalArgumentException("Unknown expression type: " + expression.getClass());
    }
}
