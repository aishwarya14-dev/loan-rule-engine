package com.aishwarya.FinBank.ruleengine.model.condition;

public class AndExpression implements  Expression {
    private Expression left;
    private Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
