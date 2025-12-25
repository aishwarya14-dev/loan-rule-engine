// Generated from LoanRules.g4 by ANTLR 4.13.1

package com.aishwarya.FinBank.antlr4;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LoanRulesParser}.
 */
public interface LoanRulesListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LoanRulesParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(LoanRulesParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link LoanRulesParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(LoanRulesParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(LoanRulesParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(LoanRulesParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code conditionExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConditionExpression(LoanRulesParser.ConditionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code conditionExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConditionExpression(LoanRulesParser.ConditionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(LoanRulesParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(LoanRulesParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(LoanRulesParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(LoanRulesParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LoanRulesParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(LoanRulesParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LoanRulesParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(LoanRulesParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LoanRulesParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(LoanRulesParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LoanRulesParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(LoanRulesParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LoanRulesParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(LoanRulesParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link LoanRulesParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(LoanRulesParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LoanRulesParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(LoanRulesParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LoanRulesParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(LoanRulesParser.ActionContext ctx);
}