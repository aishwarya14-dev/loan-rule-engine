package com.aishwarya.Finbank.validator;

import com.aishwarya.Finbank.LoanRulesLexer;
import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.exceptions.DslValidationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class DslSyntaxValidator {

    private RuleEngineMetrics metrics;

    public LoanRulesParser.StatementContext validate(String dslRule) {
        List<String> errors = new ArrayList<>();
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
                log.error("Lexer error at {}:{} — {}", line, charPositionInLine, msg);
                metrics.incrementSyntaxFailed();
            }
        });
        if (!errors.isEmpty()) {
            throw new DslValidationException(errors);
        }
        return getLoanRulesParser(lexer);

    }

    private LoanRulesParser.StatementContext getLoanRulesParser(LoanRulesLexer lexer) {
        List<String> errors = new ArrayList<>();
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
                log.error("Parser error at {}:{} — {}", line, charPositionInLine, msg);
                metrics.incrementSyntaxFailed();
            }
        });
        LoanRulesParser.StatementContext tree = parser.statement();
        if (!errors.isEmpty()) {
            throw new DslValidationException(errors);
        }
        return tree;
    }

}
