package com.aishwarya.FinBank.ruleengine.parser;

import com.aishwarya.FinBank.LoanRulesBaseVisitor;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.ruleengine.model.Action;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.model.RuleType;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.model.condition.CompositeCondition;
import com.aishwarya.FinBank.ruleengine.model.Logic;
import com.aishwarya.FinBank.ruleengine.model.condition.SimpleCondition;

import java.util.ArrayList;
import java.util.List;

public class LoanRulesVisitor extends LoanRulesBaseVisitor<Rule> {

    @Override
    public Rule visitStatement(LoanRulesParser.StatementContext ctx) {
        List<Condition> conditions = new ArrayList<>();
        Condition condition = getConditionFromExpression(ctx.expression());
        Action action = getActionFromContext(ctx.action());
        Logic logic = getLogicFromExpression(ctx.expression());
        RuleType ruleType = getRuleTypeFromExpression(ctx.expression());
        Rule rule = new Rule(condition, action, logic, ruleType);
        return rule;
    }


    @Override
    public Rule visitCondition(LoanRulesParser.ConditionContext ctx) { return  null; }

    @Override
    public Rule visitOperator(LoanRulesParser.OperatorContext ctx) {
        return null;
    }

    @Override
    public Rule visitValue(LoanRulesParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Rule visitAction(LoanRulesParser.ActionContext ctx) {
        return null;
    }

    private Condition getConditionFromExpression(LoanRulesParser.ExpressionContext ctx) {
        if (ctx instanceof LoanRulesParser.OrExpressionContext) {
            return buildOrCondition((LoanRulesParser.OrExpressionContext) ctx);
        } else if(ctx instanceof  LoanRulesParser.AndExpressionContext){
            return buildAndCondition((LoanRulesParser.AndExpressionContext) ctx);
        } else if(ctx instanceof LoanRulesParser.ConditionExpressionContext){
            return buildCondition(((LoanRulesParser.ConditionExpressionContext) ctx).condition());
        }
        throw new IllegalArgumentException("Unknown expression type: " + ctx.getClass().getName());
    }

    private Condition buildOrCondition(LoanRulesParser.OrExpressionContext ctx) {
        CompositeCondition composite = new CompositeCondition();
        composite.setLogic(Logic.OR);

        List<Condition> conditions = new ArrayList<>();
        conditions.add(getConditionFromExpression(ctx.expression(0)));
        conditions.add(getConditionFromExpression(ctx.expression(1)));

        composite.setConditions(conditions);
        return composite;
    }

    private Condition buildAndCondition(LoanRulesParser.AndExpressionContext ctx) {
        CompositeCondition compositeCondition = new CompositeCondition();
        compositeCondition.setLogic(Logic.AND);

        List<Condition> conditions = new ArrayList<>();
        conditions.add(getConditionFromExpression(ctx.expression(0)));
        conditions.add(getConditionFromExpression(ctx.expression(1)));

        compositeCondition.setConditions(conditions);
        return compositeCondition;
    }

    private Condition buildCondition(LoanRulesParser.ConditionContext ctx) {
        String field = ctx.IDENTIFIER().getText();
        String operator = getOperatorString(ctx.operator());
        Object value = getValueFromContext(ctx.value());

        return new SimpleCondition(field, operator, value);
    }

    private String getOperatorString(LoanRulesParser.OperatorContext ctx) {
        if (ctx.GT() != null) return ">";
        if (ctx.GE() != null) return ">=";
        if (ctx.LT() != null) return "<";
        if (ctx.LE() != null) return "<=";
        if (ctx.EQ() != null) return "==";
        if (ctx.NEQ() != null) return "!=";
        throw new IllegalArgumentException("Unknown operator");
    }

    private Object getValueFromContext(LoanRulesParser.ValueContext ctx) {
        return Integer.parseInt(ctx.NUMBER().getText());
    }

    private Action getActionFromContext(LoanRulesParser.ActionContext action) {
        if (action.APPROVE() != null) return Action.APPROVE;
        if (action.REJECT() != null) return Action.REJECT;
        if (action.REVIEW() != null) return Action.REVIEW;
        throw new IllegalArgumentException("Unknown action");
    }

    private Logic getLogicFromExpression(LoanRulesParser.ExpressionContext expression) {
        if (expression instanceof LoanRulesParser.OrExpressionContext) {
            return Logic.OR;
        } else if (expression instanceof LoanRulesParser.AndExpressionContext) {
            return Logic.AND;
        } else {
            return null;// or throw an exception if appropriate
        }
    }

    private RuleType getRuleTypeFromExpression(LoanRulesParser.ExpressionContext expression) {
        if (expression instanceof LoanRulesParser.OrExpressionContext) {
            return RuleType.SIMPLE;
        } else if (expression instanceof LoanRulesParser.AndExpressionContext) {
            return RuleType.COMPOSITE;
        } else {
            return RuleType.SIMPLE;
        }
    }

}
