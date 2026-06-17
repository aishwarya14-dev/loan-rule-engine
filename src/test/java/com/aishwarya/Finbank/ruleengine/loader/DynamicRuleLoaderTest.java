package com.aishwarya.Finbank.ruleengine.loader;

import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.repository.RuleRepository;
import com.aishwarya.Finbank.ruleengine.loader.DynamicRuleLoader;
import com.aishwarya.Finbank.ruleengine.parser.DslRulesParser;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DynamicRuleLoaderTest {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private DslRulesParser dslRulesParser;

    @InjectMocks
    private DynamicRuleLoader dynamicRuleLoader;

    private LoanType homeLoanType;
    private DslRule dslRule1;
    private DslRule dslRule2;
    private Rule rule1;
    private Rule rule2;


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
    }

    //happy path
    @Test
    public void shouldPassForLoadRules(){

        when(ruleRepository.findByLoanTypeLoanType("HOME_LOAN")).thenReturn(List.of(dslRule1,dslRule2));
        when(dslRulesParser.parseDslRule("IF creditScore > 700 THEN approve")).thenReturn(rule1);
        when(dslRulesParser.parseDslRule("IF monthlyIncome >= 50000 THEN approve")).thenReturn(rule2);

        List<Rule> rules = dynamicRuleLoader.loadRules(homeLoanType);

        assertThat(rules).hasSize(2);
        assertThat(rules).containsExactly(rule1, rule2);
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
    public void shouldSkipFailedRuleAndContinueLoadingRest(){
        DslRule validDslRule   = dslRule("IF creditScore > 700 THEN approve");
        DslRule invalidDslRule = dslRule("IF ??? THEN approve");
        Rule validRule = mock(Rule.class);

        when(ruleRepository.findByLoanTypeLoanType("HOME_LOAN")).thenReturn(List.of(validDslRule,invalidDslRule));
        when(dslRulesParser.parseDslRule("IF creditScore > 700 THEN approve")).thenReturn(validRule);
        when(dslRulesParser.parseDslRule("IF ??? THEN approve")).thenThrow(new RuntimeException("Parse failed"));

        List<Rule> result = dynamicRuleLoader.loadRules(homeLoanType);

        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(validRule);
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
