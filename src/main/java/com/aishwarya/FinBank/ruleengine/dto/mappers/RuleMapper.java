package com.aishwarya.FinBank.ruleengine.dto.mappers;

import com.aishwarya.FinBank.ruleengine.dto.*;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.model.RuleType;
import com.aishwarya.FinBank.ruleengine.model.condition.AndExpression;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.model.condition.Expression;
import com.aishwarya.FinBank.ruleengine.model.condition.OrExpression;
import com.aishwarya.FinBank.utility.Operator;
import org.springframework.stereotype.Component;

@Component
public class RuleMapper {
    public Rule toRule(RuleDto ruleDto) {
        Expression expression = toExpression(ruleDto.getExpression());
        RuleType type = (expression instanceof Condition)
                ? RuleType.SIMPLE
                : RuleType.COMPOSITE;
        return new Rule(expression, ruleDto.getAction(), type);
    }

    private Expression toExpression(ExpressionDto expressionDto) {
        return switch (expressionDto) {
            case ConditionDto c -> new Condition(
                    c.getField(),
                    mapOperator(c.getOperator().getSymbol()),
                    c.getValue());
            case AndExpressionDto a -> new AndExpression(
                    toExpression(a.getLeft()),
                    toExpression(a.getRight()));
            case OrExpressionDto o  -> new OrExpression(
                    toExpression(o.getLeft()),
                    toExpression(o.getRight()));
            default -> throw new IllegalArgumentException(
                    "Unknown expression type: " + expressionDto.getClass());
        };
    }

    private Operator mapOperator(String symbol) {
        return switch (symbol) {
            case ">" -> Operator.GT;
            case ">=" -> Operator.GTE;
            case "<" -> Operator.LT;
            case "<=" -> Operator.LTE;
            case "==" -> Operator.EQ;
            case "!=" -> Operator.NE;
            default -> throw new IllegalArgumentException("Unknown operator: " + symbol);
        };
    }
}
