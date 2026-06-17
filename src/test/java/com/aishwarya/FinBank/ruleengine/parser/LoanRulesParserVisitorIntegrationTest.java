package com.aishwarya.FinBank.ruleengine.parser;
import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.model.Action;
import com.aishwarya.FinBank.model.Rule;
import com.aishwarya.FinBank.model.RuleType;
import com.aishwarya.FinBank.model.expression.AndExpression;
import com.aishwarya.FinBank.model.expression.Condition;
import com.aishwarya.FinBank.model.expression.OrExpression;
import com.aishwarya.FinBank.model.value.DoubleValue;
import com.aishwarya.FinBank.model.value.IntValue;
import com.aishwarya.FinBank.model.value.StringValue;
import com.aishwarya.FinBank.utility.Operator;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static org.junit.jupiter.api.Assertions.*;

public class LoanRulesParserVisitorIntegrationTest {


   private Rule parse(String dslRule){
      LoanRulesLexer lexer = new LoanRulesLexer(CharStreams.fromString(dslRule));
      CommonTokenStream tokensStream = new CommonTokenStream(lexer);
      LoanRulesParser parser = new LoanRulesParser(tokensStream);
      LoanRulesVisitor visitor = new LoanRulesVisitor();

      return visitor.visitStatement(parser.statement());
   }

   @Test
   void shouldBuildSimpleRule() {
      Rule rule = parse("IF creditScore > 700 THEN approve");
      assertEquals(RuleType.SIMPLE, rule.getType());
      assertEquals(Action.APPROVE, rule.getAction());

      Condition condition = (Condition) rule.getExpression();

      assertEquals("creditScore", condition.getField());
      assertEquals(Operator.GT, condition.getOperator());

      IntValue value = (IntValue) condition.getValue();
      assertEquals(700, value.value());
   }

   @Test
   void shouldBuildAndExpression() {
      Rule rule = parse("IF creditScore > 700 AND income >= 50000 THEN approve");
      assertEquals(RuleType.COMPOSITE, rule.getType());
      assertTrue(rule.getExpression() instanceof AndExpression);

      AndExpression expression = (AndExpression) rule.getExpression();

      assertNotNull(expression.getLeft());
      assertNotNull(expression.getRight());
   }

   @Test
   void shouldBuildOrExpression() {
      Rule rule =parse("IF creditScore > 700 OR income >= 50000 THEN review");
      assertEquals(Action.REVIEW, rule.getAction());
      assertTrue(rule.getExpression() instanceof OrExpression);
   }

   @Test
   void shouldHandleParentheses() {
      Rule rule = parse("IF (creditScore > 700 AND income >= 50000) THEN approve");
      assertTrue(rule.getExpression() instanceof AndExpression);
   }

   @Test
   void shouldParseDoubleValue() {
      Rule rule = parse("IF interestRate <= 7.5 THEN approve");
      Condition condition = (Condition) rule.getExpression();
      assertEquals(condition.getValue(), new com.aishwarya.FinBank.model.value.DoubleValue(7.5));
   }

   @Test
   void shouldParseStringValue() {
      Rule rule = parse("IF employmentType == 'SALARIED' THEN approve");
      Condition condition = (Condition) rule.getExpression();
      StringValue value = (StringValue) condition.getValue();
      assertEquals( "SALARIED",value.value().replace("'", ""));
   }

   @ParameterizedTest
   @CsvSource({
           "'>', GT",
           "'>=', GTE",
           "'<', LT",
           "'<=', LTE",
           "'==', EQ",
           "'!=', NE"
   })
   void shouldMapOperators(String op, Operator expected) {
      Rule rule = parse("IF creditScore " + op + " 700 THEN approve");
      Condition condition = (Condition) rule.getExpression();
      assertEquals(expected, condition.getOperator());
   }
}
