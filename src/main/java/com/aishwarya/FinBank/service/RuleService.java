package com.aishwarya.FinBank.service;


import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Service;

@Service
public class RuleService {
    public void processRules(RulesRequestDto rulesRequestDto) {
        CharStream charStream = CharStreams.fromString(rulesRequestDto.getRuleText());
//        LoanRulesLexer lexer = new LoanRulesLexer(charStream);
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        LoanRulesParser parser = new LoanRulesParser(tokens);

    }
}
