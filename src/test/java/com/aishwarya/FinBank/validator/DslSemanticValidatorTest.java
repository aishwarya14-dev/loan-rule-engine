package com.aishwarya.FinBank.validator;

import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.exceptions.DslValidationException;
import com.aishwarya.FinBank.repository.EmploymentTypeRepo;
import com.aishwarya.FinBank.repository.JobTitleRepo;
import com.aishwarya.FinBank.repository.LoanTypeRepo;
import com.aishwarya.FinBank.repository.RegionRepo;
import com.aishwarya.FinBank.ruleValidator.DslSemanticValidator;
import com.aishwarya.FinBank.utility.LoanFieldAccessorRegistry;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DslSemanticValidatorTest {

    @Mock
    private LoanFieldAccessorRegistry registry;

    @Mock
    private RegionRepo regionRepository;

    @Mock
    private EmploymentTypeRepo employmentTypeRepository;

    @Mock
    private JobTitleRepo jobTitleRepository;

    @Mock
    private LoanTypeRepo loanTypeRepository;

    @InjectMocks
    private DslSemanticValidator semanticValidator;


    private LoanRulesParser.StatementContext parseRule(String dsl){
        CharStream chars         = CharStreams.fromString(dsl);
        LoanRulesLexer lexer     = new LoanRulesLexer(chars);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LoanRulesParser parser   = new LoanRulesParser(tokens);
        return parser.statement();
    }

    @Test
    void shouldPassForValidSimpleRule(String dsl){
        Assertions.assertDoesNotThrow(() -> semanticValidator.validate(parseRule("IF creditScore > 700 THEN approve")));
    }


    @Test
    void shouldPassForValidCompositeRule(String dsl){
        Assertions.assertDoesNotThrow(() -> semanticValidator.validate(parseRule("IF creditScore > 700 AND monthlyIncome >= 50000 THEN approve")));
    }

    @Test
    void shouldFailForInvalidField(String dsl){
        Assertions.assertThrows(DslValidationException.class,() -> semanticValidator.validate(parseRule("IF points > 700 THEN approve")));
    }
}
