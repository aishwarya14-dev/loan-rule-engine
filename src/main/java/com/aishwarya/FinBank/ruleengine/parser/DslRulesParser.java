package com.aishwarya.Finbank.ruleengine.parser;

import com.aishwarya.Finbank.LoanRulesLexer;
import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.exceptions.DslParsingException;
import com.aishwarya.Finbank.model.Rule;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.springframework.stereotype.Component;


@Component
public class DslRulesParser {
    public Rule parseDslRule(String dslRule) {
        if (dslRule == null)
            throw new DslParsingException("DSL rule cannot be null");

        try {
            CharStream charStream = CharStreams.fromString(dslRule);
            LoanRulesLexer lexer = new LoanRulesLexer(charStream);
            CommonTokenStream tokensStream = new CommonTokenStream(lexer);
            LoanRulesParser parser = new LoanRulesParser(tokensStream);
            LoanRulesParser.StatementContext tree = parser.statement();
            System.out.println(tree == null);
            if (parser.getNumberOfSyntaxErrors() > 0) {
                throw new DslParsingException("DSL rule is not valid");
            }
            LoanRulesVisitor visitor = new LoanRulesVisitor();

            return visitor.visitStatement(tree);

        } catch (ClassCastException e) {
            throw new DslParsingException("Internal error while building rule from: " + dslRule);
        } catch (IllegalArgumentException e) {
            throw new DslParsingException(e.getMessage());
        } catch (Exception e) {
            throw new DslParsingException("Failed to parse DSL rule: " + e.getMessage());
        }
    }
}
