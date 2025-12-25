package com.aishwarya.FinBank.service;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.stereotype.Service;
import com.aishwarya.FinBank.antlr4.LoanRulesLexer;
import com.aishwarya.FinBank.antlr4.LoanRulesParser;
import com.aishwarya.FinBank.service.parser.LoanRulesVisitorImpl;

@Service
public class DslRulesParserService {
    public String parseDslRule(String dslRule) {

        CharStream charStream = CharStreams.fromString(dslRule);
        LoanRulesLexer lexer = new LoanRulesLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LoanRulesParser parser = new LoanRulesParser(tokens);
        ParseTree tree = parser.statement();

        LoanRulesVisitorImpl visitor = new LoanRulesVisitorImpl();
        return visitor.visit(tree);
    }
}
