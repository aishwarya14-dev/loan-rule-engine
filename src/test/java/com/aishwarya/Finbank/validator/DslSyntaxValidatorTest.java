package com.aishwarya.Finbank.validator;

import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.exceptions.DslValidationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.validator.DslSyntaxValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DslSyntaxValidatorTest {

    private DslSyntaxValidator dslSyntaxValidator;

    @Mock
    private RuleEngineMetrics metrics;

    @BeforeEach
    void setUp() {
        dslSyntaxValidator = new DslSyntaxValidator(metrics);
    }

    // simple condition
    @Test
    void shouldPassForSimpleRule() {
        assertDoesNotThrow(() ->
                dslSyntaxValidator.validate("IF creditScore > 700 THEN approve")
        );
    }

    // composite condition
    @Test
    void shouldPassForCompositeRule(){
        assertDoesNotThrow(() ->
                dslSyntaxValidator.validate("IF creditScore > 700 AND monthlyIncome >= 50000 THEN approve")
                );
    }

    // validate returns StatementContext for reuse in semantic validator
    @Test
    void shouldReturnParseTree() {
        LoanRulesParser.StatementContext tree =
                dslSyntaxValidator.validate("IF creditScore > 700 THEN approve");

        assertNotNull(tree);
        assertNotNull(tree.expression());
        assertNotNull(tree.action());
    }

    @Test
    void shouldFailWhenIfIsMissing() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("creditScore > 700 THEN approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenThenIsMissing() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF creditScore > 700 approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenExpressionIsMissing() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF THEN approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenActionIsMissing() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF creditScore > 700 THEN")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailForInvalidOperator() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF creditScore >> 700 THEN approve")
        );
        assertFalse(ex.getErrors().isEmpty());
        assertTrue(ex.getErrors().get(0).contains("error"));
    }

    @Test
    void shouldFailForInvalidAction() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF creditScore > 700 THEN deny")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailForEmptyString() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF THEN")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailForMissingValue() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF creditScore > THEN approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailForMissingField() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF > 700 THEN approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    void shouldFailForUnclosedParenthesis() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF (creditScore > 700 AND monthlyIncome >= 50000 THEN approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    // AND with nothing on the right
    @Test
    void shouldFailForIncompleteAndExpression() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("IF creditScore > 700 AND THEN approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    // both IF and THEN missing
    @Test
    void shouldCollectMultipleErrors() {
        DslValidationException ex = assertThrows(DslValidationException.class, () ->
                dslSyntaxValidator.validate("creditScore >> 700 approve")
        );
        assertFalse(ex.getErrors().isEmpty());
    }


}
