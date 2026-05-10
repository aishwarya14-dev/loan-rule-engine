package com.aishwarya.FinBank.ruleengine.parser;

import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.model.Rule;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Component;


@Component
public class DslRulesParser {
    public Rule parseDslRule(String dslRule) {

        CharStream charStream = CharStreams.fromString(dslRule);
        LoanRulesLexer lexer = new LoanRulesLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LoanRulesParser parser = new LoanRulesParser(tokens);
        LoanRulesParser.StatementContext tree = parser.statement();

        LoanRulesVisitor visitor = new LoanRulesVisitor();
        return visitor.visitStatement(tree);
    }
}
