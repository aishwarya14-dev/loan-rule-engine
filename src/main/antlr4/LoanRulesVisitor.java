// Generated from LoanRules.g4 by ANTLR 4.13.1

package com.aishwarya.FinBank.antlr4;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LoanRulesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LoanRulesVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LoanRulesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(LoanRulesParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(LoanRulesParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionExpression(LoanRulesParser.ConditionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(LoanRulesParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link LoanRulesParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpression(LoanRulesParser.ParenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LoanRulesParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(LoanRulesParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LoanRulesParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(LoanRulesParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LoanRulesParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(LoanRulesParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link LoanRulesParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(LoanRulesParser.ActionContext ctx);
}