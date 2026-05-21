package com.aishwarya.FinBank.ruleengine.parser;

import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.exceptions.DslParsingException;
import com.aishwarya.FinBank.model.Rule;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.springframework.stereotype.Component;


@Component
public class DslRulesParser {
    public Rule parseDslRule(String dslRule) {
        try
        {
            CharStream charStream = CharStreams.fromString(dslRule);
            LoanRulesLexer lexer = new LoanRulesLexer(charStream);
            CommonTokenStream tokensStream = new CommonTokenStream(lexer);
            LoanRulesParser parser = new LoanRulesParser(tokensStream);
            LoanRulesParser.StatementContext tree = parser.statement();
            LoanRulesVisitor visitor = new LoanRulesVisitor();

            return visitor.visitStatement(tree);

        } catch (NullPointerException e){
            throw new DslParsingException("DSL rule cannot be null" + e.getMessage());
        } catch (RecognitionException e){
            throw new DslParsingException("DSL rule is not valid: " + e.getMessage());
        } catch (ClassCastException e){
            throw new DslParsingException("Internal error while building rule from: " + dslRule);
        } catch (IllegalArgumentException e) {
            throw new DslParsingException(e.getMessage());
        } catch (Exception e) {
            throw new DslParsingException("Failed to parse DSL rule: " + e.getMessage());
        }
    }
}
