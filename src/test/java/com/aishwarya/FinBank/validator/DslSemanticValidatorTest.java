package com.aishwarya.Finbank.validator;

import com.aishwarya.Finbank.LoanRulesLexer;
import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.exceptions.DslValidationException;
import com.aishwarya.Finbank.repository.EmploymentTypeRepo;
import com.aishwarya.Finbank.repository.JobTitleRepo;
import com.aishwarya.Finbank.repository.LoanTypeRepo;
import com.aishwarya.Finbank.repository.RegionRepo;
import com.aishwarya.Finbank.validator.DslSemanticValidator;
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

//    @Test
//    void shouldPassForValidSimpleRule(){
//        Assertions.assertDoesNotThrow(() -> semanticValidator.validate(parseRule("IF creditScore > 700 THEN approve")));
//    }
//
//
//    @Test
//    void shouldPassForValidCompositeRule(){
//        Assertions.assertDoesNotThrow(() -> semanticValidator.validate(parseRule("IF creditScore > 700 AND monthlyIncome >= 50000 THEN approve")));
//    }

    @Test
    void shouldFailForInvalidField(){
        Assertions.assertThrows(DslValidationException.class,() -> semanticValidator.validate(parseRule("IF points > 700 THEN approve")));
    }
}
