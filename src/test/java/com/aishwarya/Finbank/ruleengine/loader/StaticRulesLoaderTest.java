package com.aishwarya.Finbank.ruleengine.loader;

import com.aishwarya.Finbank.dto.rules.staticrules.StaticRule;
import com.aishwarya.Finbank.mapper.RuleMapper;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.model.Rule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaticRulesLoaderTest {

    @InjectMocks
    private StaticRuleLoader staticRuleLoader;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RuleMapper ruleMapper;

    private LoanType loanType(String name){
        LoanType loanType = new LoanType();
        loanType.setId(1L);
        loanType.setLoanType(name);
        return loanType;
    }


    @Test
    void shouldLoadRulesSuccessfully() throws Exception {
        StaticRule dto = new StaticRule();
        Rule rule = mock(Rule.class);

        when(objectMapper.readValue(
                any(InputStream.class),
                any(TypeReference.class)
        )).thenReturn(List.of(dto));

        when(ruleMapper.toRule(dto)).thenReturn(rule);
        LoanType homeLoanType = loanType("HOME_LOAN");

        List<Rule> rules = staticRuleLoader.loadRules(homeLoanType);
        assertEquals(1, rules.size());
        verify(ruleMapper).toRule(dto);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenJsonCannotBeRead()
            throws Exception {
        when(objectMapper.readValue(
                any(InputStream.class),
                any(TypeReference.class))).thenThrow(new IOException("File not found"));
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> staticRuleLoader.loadRules(loanType("HOME_LOAN"))
        );
        assertTrue(
                exception.getMessage()
                        .contains("Failed to load rules")
        );
    }
}
