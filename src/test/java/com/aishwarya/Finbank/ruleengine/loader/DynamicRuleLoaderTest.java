package com.aishwarya.Finbank.ruleengine.loader;

import com.aishwarya.Finbank.exceptions.DslParsingException;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.repository.RuleRepository;
import com.aishwarya.Finbank.ruleengine.loader.DynamicRuleLoader;
import com.aishwarya.Finbank.ruleengine.parser.DslRulesParser;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DynamicRuleLoaderTest {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private DslRulesParser dslRulesParser;

    @Mock
    private LoanTypeFactorConfigService loanTypeFactorConfigService;

    @InjectMocks
    private DynamicRuleLoader dynamicRuleLoader;

    private LoanType homeLoanType;
    private DslRule dslRule1;
    private DslRule dslRule2;
    private Rule rule1;
    private Rule rule2;
    private LoanTypeFactorConfig loanTypeFactorConfig;


    private LoanType loanType(String name){
       LoanType loanType = new LoanType();
       loanType.setId(1L);
       loanType.setLoanType(name);
       return loanType;
    }

    private DslRule dslRule(String ruleText){
        DslRule rule = new DslRule();
        rule.setDslRule(ruleText);
        return rule;
    }

    @BeforeEach
    public void setup(){
        homeLoanType = loanType("HOME_LOAN");
        dslRule1 = dslRule("IF creditScore > 700 THEN approve");
        dslRule2 = dslRule("IF monthlyIncome >= 50000 THEN approve");

        rule1 = mock(Rule.class);
        rule2 = mock(Rule.class);

        loanTypeFactorConfig = mock(LoanTypeFactorConfig.class);
    }

    //happy path
    @Test
    void shouldLoadAndEnrichRulesSuccessfully() {
        when(ruleRepository.findByLoanTypeLoanType("HOME_LOAN"))
                .thenReturn(List.of(dslRule1, dslRule2));

        when(dslRulesParser.parseDslRule(dslRule1.getDslRule()))
                .thenReturn(rule1);
        when(dslRulesParser.parseDslRule(dslRule2.getDslRule()))
                .thenReturn(rule2);

        // Real LoanType
        LoanType loanType = new LoanType();
        loanType.setId(1L);
        loanType.setLoanType("HOME_LOAN");

        // Real Factor
        Factor factor = new Factor();
        factor.setId(10L);

        // Attach to DSL rules
        dslRule1.setLoanType(loanType);
        dslRule2.setLoanType(loanType);

        dslRule1.setFactor(factor);
        dslRule2.setFactor(factor);

        // Real ImportanceLevel
        ImportanceLevel importanceLevel = new ImportanceLevel();
        importanceLevel.setWeight(5);

        // Real LoanTypeFactorConfig
        LoanTypeFactorConfig config = new LoanTypeFactorConfig();
        config.setLoanType(loanType);
        config.setFactor(factor);
        config.setImportanceLevel(importanceLevel);

        when(loanTypeFactorConfigService.getLoanTypeFactorConfig(1L, 10L))
                .thenReturn(config);

        // Act
        List<Rule> rules = dynamicRuleLoader.loadRules(homeLoanType);

        // Assert
        assertThat(rules).hasSize(2);
        assertThat(rules).containsExactly(rule1, rule2);

        verify(ruleRepository).findByLoanTypeLoanType("HOME_LOAN");
        verify(dslRulesParser).parseDslRule(dslRule1.getDslRule());
        verify(dslRulesParser).parseDslRule(dslRule2.getDslRule());

        verify(loanTypeFactorConfigService, times(2))
                .getLoanTypeFactorConfig(1L, 10L);
    }

    @Test
    public void shouldReturnEmptyListWhenNoRulesFoundForLoanType(){
        LoanType carLoan = loanType("CAR_LOAN");
        when(ruleRepository.findByLoanTypeLoanType("CAR_LOAN")).thenReturn(Collections.emptyList());

        List<Rule> rules = dynamicRuleLoader.loadRules(carLoan);

        assertThat(rules).hasSize(0);
    }

    //when one rule fails to parse
    @Test
    void shouldSkipFailedRuleAndContinueLoadingRest() {
        when(ruleRepository.findByLoanTypeLoanType("HOME_LOAN"))
                .thenReturn(List.of(dslRule1, dslRule2));

        when(dslRulesParser.parseDslRule(dslRule1.getDslRule()))
                .thenThrow(new DslParsingException("Invalid DSL"));

        when(dslRulesParser.parseDslRule(dslRule2.getDslRule()))
                .thenReturn(rule2);

        LoanType loanType = new LoanType();
        loanType.setId(1L);
        loanType.setLoanType("HOME_LOAN");

        Factor factor = new Factor();
        factor.setId(10L);

        dslRule2.setLoanType(loanType);
        dslRule2.setFactor(factor);

        ImportanceLevel importanceLevel = new ImportanceLevel();
        importanceLevel.setWeight(5);

        LoanTypeFactorConfig config = new LoanTypeFactorConfig();
        config.setLoanType(loanType);
        config.setFactor(factor);
        config.setImportanceLevel(importanceLevel);

        when(loanTypeFactorConfigService.getLoanTypeFactorConfig(1L, 10L))
                .thenReturn(config);

        List<Rule> rules = dynamicRuleLoader.loadRules(homeLoanType);

        assertThat(rules)
                .hasSize(1)
                .containsExactly(rule2);

        verify(dslRulesParser).parseDslRule(dslRule1.getDslRule());
        verify(dslRulesParser).parseDslRule(dslRule2.getDslRule());

        verify(loanTypeFactorConfigService)
                .getLoanTypeFactorConfig(1L, 10L);
    }

    //when fail to parse all the rules
    @Test
    void shouldReturnEmptyListWhenAllRulesFailToParse(){
        DslRule badRule1 = dslRule("IF ??? THEN approve");
        DslRule badRule2 = dslRule("IF @@@ THEN reject");

        when(ruleRepository.findByLoanTypeLoanType("HOME_LOAN")).thenReturn(List.of(badRule1,badRule2));

        when(dslRulesParser.parseDslRule(any()))
                .thenThrow(new RuntimeException("Parse failed"));

        // when
        List<Rule> result = dynamicRuleLoader.loadRules(homeLoanType);

        // then
        assertThat(result).isEmpty();
    }

    //when repo throws exception
    @Test
    void shouldReturnEmptyListWhenRepositoryThrows(){
        when(ruleRepository.findByLoanTypeLoanType("HOME_LOAN")).thenReturn(List.of());
        List<Rule> result = dynamicRuleLoader.loadRules(homeLoanType);
        assertThat(result).isEmpty();

    }

}
