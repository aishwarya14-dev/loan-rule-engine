package com.aishwarya.FinBank.ruleValidator;

import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.exceptions.DslValidationException;
import org.antlr.v4.runtime.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DslSyntaxValidator {
    List<String> errors = new ArrayList<>();

    public LoanRulesParser.StatementContext validate(String dslRule) {
        CharStream input = CharStreams.fromString(dslRule);
        LoanRulesParser loanRulesParser = getLoanRulesParser(input);
        loanRulesParser.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e){
                errors.add("Parser error at " + line + ":"
                        + charPositionInLine + " — " + msg);
            }
        });
        LoanRulesParser.StatementContext tree = loanRulesParser.statement();
        if (!errors.isEmpty()) {
            throw new DslValidationException(errors);
        }

        return tree;

    }

    private LoanRulesParser getLoanRulesParser(CharStream input) {
        LoanRulesLexer lexer = new LoanRulesLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                     Object offendingSymbol,
                                     int line, int charPositionInLine,
                                     String msg, RecognitionException e){
                errors.add("Lexer error at " + line + ":"
                        + charPositionInLine + " — " + msg);
            }
        });

        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        LoanRulesParser loanRulesParser = new LoanRulesParser(commonTokenStream);

        loanRulesParser.removeErrorListeners();
        return loanRulesParser;
    }

}
