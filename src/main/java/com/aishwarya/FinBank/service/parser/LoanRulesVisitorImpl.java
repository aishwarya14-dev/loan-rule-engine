package com.aishwarya.FinBank.service.parser;

import com.aishwarya.FinBank.LoanRulesBaseVisitor;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.RuleEvaluation;

public class LoanRulesVisitorImpl extends LoanRulesBaseVisitor<RuleEvaluation> {

    public RuleEvaluation visit(LoanRulesParser.StatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitStatement(LoanRulesParser.StatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitOrExpression(LoanRulesParser.OrExpressionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitConditionExpression(LoanRulesParser.ConditionExpressionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitAndExpression(LoanRulesParser.AndExpressionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitParenExpression(LoanRulesParser.ParenExpressionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitCondition(LoanRulesParser.ConditionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitOperator(LoanRulesParser.OperatorContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitValue(LoanRulesParser.ValueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public RuleEvaluation visitAction(LoanRulesParser.ActionContext ctx) {
        return visitChildren(ctx);
    }
}
