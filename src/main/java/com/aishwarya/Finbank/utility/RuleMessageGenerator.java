package com.aishwarya.Finbank.utility;

import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.model.expression.AndExpression;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.model.expression.OrExpression;
import org.springframework.stereotype.Component;

@Component
public class RuleMessageGenerator {

    public String generateMessage(String field, String operator,
                                  Object expectedValue, Object actualValue,
                                  boolean passed) {
        String rule = field + " " + operator + " " + expectedValue;
        if (passed) {
            return String.format("Rule '%s' passed ", rule);
        } else {
            return String.format(
                    "Rule '%s' failed (Expected: %s %s %s, Actual: %s) ",
                    rule, field, operator, expectedValue, actualValue
            );
        }
    }

    // Overload for composite rules (AND/OR summary)
    public String generateMessage(Rule rule , boolean passed) {
        if (passed) {
            return String.format("Composite rule '%s' passed ", expressionToText(rule.getExpression()));
        } else {
            return String.format("Composite rule '%s' failed ", expressionToText(rule.getExpression()));
        }
    }

    private String expressionToText(Expression expression) {
        if (expression instanceof Condition c) {
            return c.getField() + " " +
                    c.getOperator().getSymbol() + " " +
                    c.getValue();
        }
        if (expression instanceof AndExpression a) {
            return "(" +
                    expressionToText(a.getLeft()) +
                    " AND " +
                    expressionToText(a.getRight()) +
                    ")";
        }
        if (expression instanceof OrExpression o) {
            return "(" +
                    expressionToText(o.getLeft()) +
                    " OR " +
                    expressionToText(o.getRight()) +
                    ")";
        }
        throw new IllegalArgumentException();
    }
}

