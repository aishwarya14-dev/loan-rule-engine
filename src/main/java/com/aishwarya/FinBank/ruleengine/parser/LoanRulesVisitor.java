package com.aishwarya.FinBank.ruleengine.parser;

import com.aishwarya.FinBank.LoanRulesBaseVisitor;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.model.Action;
import com.aishwarya.FinBank.model.Rule;
import com.aishwarya.FinBank.model.RuleType;
import com.aishwarya.FinBank.model.expression.AndExpression;
import com.aishwarya.FinBank.model.expression.Condition;
import com.aishwarya.FinBank.model.expression.Expression;
import com.aishwarya.FinBank.model.expression.OrExpression;
import com.aishwarya.FinBank.model.value.RuleValue;
import com.aishwarya.FinBank.utility.Operator;


public class LoanRulesVisitor extends LoanRulesBaseVisitor<Object> {

    @Override
    public Rule visitStatement(LoanRulesParser.StatementContext ctx) {
        Expression expression = (Expression) visit(ctx.expression());
        Action action = (Action) visit(ctx.action());
        RuleType type = (expression instanceof Condition) ? RuleType.SIMPLE : RuleType.COMPOSITE;
        return new Rule(expression, action, type);
    }

    @Override
    public Rule visitValue(LoanRulesParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Expression visitOrExpression(LoanRulesParser.OrExpressionContext ctx) {
        Expression left  = (Expression) visit(ctx.expression(0));
        Expression right = (Expression) visit(ctx.expression(1));
        return new OrExpression(left, right);
    }

    @Override
    public Expression visitAndExpression(LoanRulesParser.AndExpressionContext ctx) {
        Expression left  = (Expression) visit(ctx.expression(0));
        Expression right = (Expression) visit(ctx.expression(1));
        return new AndExpression(left, right);
    }

    @Override
    public Expression visitParenExpression(LoanRulesParser.ParenExpressionContext ctx) {
        return (Expression) visit(ctx.expression());
    }

    @Override
    public Expression visitConditionExpression(LoanRulesParser.ConditionExpressionContext ctx) {
        return (Expression) visit(ctx.condition());
    }

    @Override
    public Condition visitCondition(LoanRulesParser.ConditionContext ctx) {
        String field    = ctx.IDENTIFIER().getText();
        Operator op     = (Operator) visit(ctx.operator());
        RuleValue value = ctx.value() != null ? (RuleValue) visit(ctx.value()) : null;
        return new Condition(field, op, value);
    }

    @Override
    public Operator visitOperator(LoanRulesParser.OperatorContext ctx) {
        return switch (ctx.getText()) {
            case ">"  -> Operator.GT;
            case ">=" -> Operator.GTE;
            case "<"  -> Operator.LT;
            case "<=" -> Operator.LTE;
            case "==" -> Operator.EQ;
            case "!=" -> Operator.NE;
            default   -> throw new IllegalArgumentException("Unknown operator: " + ctx.getText());
        };
    }

    @Override
    public Action visitAction(LoanRulesParser.ActionContext ctx) {
        return switch (ctx.getText()) {
            case "approve" -> Action.APPROVE;
            case "reject"  -> Action.REJECT;
            case "review"  -> Action.REVIEW;
            default        -> throw new IllegalArgumentException("Unknown action: " + ctx.getText());
        };
    }

}
