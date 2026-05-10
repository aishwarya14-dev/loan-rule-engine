package com.aishwarya.FinBank.mapper;

import com.aishwarya.FinBank.dto.rules.staticrules.*;
import com.aishwarya.FinBank.model.Rule;
import com.aishwarya.FinBank.model.RuleType;
import com.aishwarya.FinBank.model.expression.AndExpression;
import com.aishwarya.FinBank.model.expression.Condition;
import com.aishwarya.FinBank.model.expression.Expression;
import com.aishwarya.FinBank.model.expression.OrExpression;
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
