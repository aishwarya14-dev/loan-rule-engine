package com.aishwarya.FinBank.ruleengine.factory;


import com.aishwarya.FinBank.ruleengine.model.Logic;
import com.aishwarya.FinBank.ruleengine.model.condition.AndExpression;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.model.condition.Expression;
import com.aishwarya.FinBank.ruleengine.model.condition.OrExpression;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.CompositeRuleEvaluation;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.SimpleRuleEvaluation;
import com.aishwarya.FinBank.utility.FieldAccessorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class CompositeRuleEvaluationFactory {

    @Autowired
    public FieldAccessorRegistry registry;

    public RuleEvaluation buildEvaluation(Expression expression) {
        if (expression instanceof Condition condition) {
            return new SimpleRuleEvaluation(
                    condition.getField(),
                    condition.getOperator().getSymbol(),
                    condition.getValue(),
                    registry
            );

        } else if (expression instanceof AndExpression andExpr) {
            List<RuleEvaluation> evaluations = List.of(
                    buildEvaluation(andExpr.getLeft()),
                    buildEvaluation(andExpr.getRight())
            );
            return new CompositeRuleEvaluation(evaluations, Logic.AND);

        } else if (expression instanceof OrExpression orExpr) {
            List<RuleEvaluation> evaluations = List.of(
                    buildEvaluation(orExpr.getLeft()),
                    buildEvaluation(orExpr.getRight())
            );
            return new CompositeRuleEvaluation(evaluations, Logic.OR);
        }

        throw new IllegalArgumentException("Unknown expression type: " + expression.getClass());
    }
}
