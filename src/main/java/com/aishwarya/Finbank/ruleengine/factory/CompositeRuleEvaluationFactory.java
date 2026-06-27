package com.aishwarya.Finbank.ruleengine.factory;


import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.repository.LoanTypeFactorConfigRepo;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
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
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CompositeRuleEvaluationFactory {

    private LoanFieldAccessorRegistry registry;

    private RuleMessageGenerator ruleMessageGenerator;

    private LoanTypeFactorConfigService loanTypeFactorConfigService;



    public RuleEvaluation buildCompositeRuleEvaluationObject(Expression expression,Rule rule) {
        if (expression instanceof Condition condition) {
            return new SimpleRuleEvaluation(
                    rule,
                    registry,
                    ruleMessageGenerator,
                    loanTypeFactorConfigService
            );
        } else if (expression instanceof AndExpression andExpr) {
            List<RuleEvaluation> evaluations = List.of(
                    buildCompositeRuleEvaluationObject(andExpr.getLeft(),rule),
                    buildCompositeRuleEvaluationObject(andExpr.getRight(),rule)
            );
            return new CompositeRuleEvaluation(evaluations, Logic.AND, ruleMessageGenerator, rule,loanTypeFactorConfigService);
        } else if (expression instanceof OrExpression orExpr) {
            List<RuleEvaluation> evaluations = List.of(
                    buildCompositeRuleEvaluationObject(orExpr.getLeft(),rule),
                    buildCompositeRuleEvaluationObject(orExpr.getRight(),rule)
            );
            return new CompositeRuleEvaluation(evaluations, Logic.OR, ruleMessageGenerator, rule,loanTypeFactorConfigService);

        }
        throw new IllegalArgumentException("Unknown expression type: " + expression.getClass());
    }
}
