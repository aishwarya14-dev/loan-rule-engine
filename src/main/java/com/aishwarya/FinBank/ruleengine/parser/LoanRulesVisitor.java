package com.aishwarya.FinBank.ruleengine.parser;

import com.aishwarya.FinBank.LoanRulesBaseVisitor;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.ruleengine.model.Action;
import com.aishwarya.FinBank.ruleengine.model.RulePOJO;
import com.aishwarya.FinBank.ruleengine.model.RuleType;
import com.aishwarya.FinBank.ruleengine.model.condition.AndExpression;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.model.condition.Expression;
import com.aishwarya.FinBank.ruleengine.model.condition.OrExpression;
import com.aishwarya.FinBank.utility.Operator;


public class LoanRulesVisitor extends LoanRulesBaseVisitor<Object> {

    @Override
    public RulePOJO visitStatement(LoanRulesParser.StatementContext ctx) {
        Expression expression = (Expression) visit(ctx.expression());
        Action action = (Action) visit(ctx.action());
        RuleType type = (expression instanceof Condition) ? RuleType.SIMPLE : RuleType.COMPOSITE;
        return new RulePOJO(expression, action, type);
    }

    @Override
    public RulePOJO visitValue(LoanRulesParser.ValueContext ctx) {
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
        int value       = Integer.parseInt(ctx.value().NUMBER().getText());
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
