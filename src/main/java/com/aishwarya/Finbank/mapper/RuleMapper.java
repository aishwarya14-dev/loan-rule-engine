package com.aishwarya.Finbank.mapper;


import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.dto.rules.staticrules.*;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.enums.RuleType;
import com.aishwarya.Finbank.model.expression.AndExpression;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.model.expression.OrExpression;
import org.springframework.stereotype.Component;

@Component
public class RuleMapper {
    public Rule toRule(StaticRule ruleDto) {
        Expression expression = toExpression(ruleDto.getExpression());
        RuleType type = (expression instanceof Condition)
                ? RuleType.SIMPLE
                : RuleType.COMPOSITE;
        Rule rule = new Rule(expression, ruleDto.getAction(), type);
        rule.setEvidenceWeight(ruleDto.getEvidenceWeight());
        return rule;
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
            case OrExpressionDto o -> new OrExpression(
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
