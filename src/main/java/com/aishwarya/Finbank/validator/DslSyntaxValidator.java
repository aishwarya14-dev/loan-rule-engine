package com.aishwarya.Finbank.validator;

import com.aishwarya.Finbank.LoanRulesLexer;
import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.exceptions.DslValidationException;
import org.antlr.v4.runtime.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DslSyntaxValidator {
    List<String> errors = new ArrayList<>();

    public LoanRulesParser.StatementContext validate(String dslRule) {
        CharStream chars = CharStreams.fromString(dslRule);
        LoanRulesLexer lexer = new LoanRulesLexer(chars);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                errors.add("Lexer error at " + line + ":"
                        + charPositionInLine + " — " + msg);
            }
        });
        if (!errors.isEmpty()) {
            throw new DslValidationException(errors);
        }
        return getLoanRulesParser(lexer);

    }

    private LoanRulesParser.StatementContext getLoanRulesParser(LoanRulesLexer lexer) {
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LoanRulesParser parser = new LoanRulesParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                errors.add("Lexer error at " + line + ":"
                        + charPositionInLine + " — " + msg);
            }
        });
        LoanRulesParser.StatementContext tree = parser.statement();
        if (!errors.isEmpty()) {
            throw new DslValidationException(errors);
        }
        return tree;
    }

}
