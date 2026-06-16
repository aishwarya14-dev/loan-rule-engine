package com.aishwarya.FinBank.ruleengine.parser;

import com.aishwarya.FinBank.exceptions.DslParsingException;
import com.aishwarya.FinBank.model.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DslRulesParserTest {

    @InjectMocks
    private DslRulesParser dslRulesParser;

    @Test
    public void parseValidDslRulesSuccessfully(){
        String dslRule = "IF creditScore > 700 THEN approve";
        Rule rule = dslRulesParser.parseDslRule(dslRule);
        assertNotNull(rule);
    }


    @Test
    void shouldThrowExceptionWhenDslRuleIsNull() {
        assertThrows(DslParsingException.class,
                () -> dslRulesParser.parseDslRule(null));
    }

    @Test
    void shouldThrowExceptionForInvalidDslRule() {
        String invalidDsl = "&&&&";
        DslParsingException exception = assertThrows(DslParsingException.class,
                () -> dslRulesParser.parseDslRule(invalidDsl));

        assertTrue(exception.getMessage().contains("Failed to parse DSL rule")
                || exception.getMessage().contains("DSL rule is not valid"));

    }
}
