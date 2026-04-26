package com.aishwarya.FinBank.service;


import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.parser.LoanRulesVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Service;

@Service
public class RuleService {
    public Rule processRules(RulesRequestDto rulesRequestDto) {
        String dslRule = rulesRequestDto.getRuleText();
        CharStream charStream = CharStreams.fromString(dslRule);
        LoanRulesLexer lexer = new LoanRulesLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LoanRulesParser parser = new LoanRulesParser(tokens);

        // Start parsing from the root rule
        LoanRulesParser.StatementContext tree = parser.statement();

        LoanRulesVisitor visitor = new LoanRulesVisitor();
        return visitor.visitStatement(tree);

    }
}
