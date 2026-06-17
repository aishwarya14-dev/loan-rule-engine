package com.aishwarya.FinBank.ruleengine.factory;


import com.aishwarya.FinBank.model.Logic;
import com.aishwarya.FinBank.model.RuleMessageGenerator;
import com.aishwarya.FinBank.model.expression.AndExpression;
import com.aishwarya.FinBank.model.expression.Condition;
import com.aishwarya.FinBank.model.expression.Expression;
import com.aishwarya.FinBank.model.expression.OrExpression;
import com.aishwarya.FinBank.ruleengine.evaluation.CompositeRuleEvaluation;
import com.aishwarya.FinBank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.evaluation.SimpleRuleEvaluation;
import com.aishwarya.FinBank.utility.LoanFieldAccessorRegistry;
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
            return new CompositeRuleEvaluation(evaluations, Logic.AND,ruleMessageGenerator);

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
