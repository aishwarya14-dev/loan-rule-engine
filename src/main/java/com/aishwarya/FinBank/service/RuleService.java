package com.aishwarya.FinBank.service;


import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import com.aishwarya.FinBank.mapper.DslRuleMapper;
import com.aishwarya.FinBank.ruleengine.dto.mappers.RuleMapper;
import com.aishwarya.FinBank.ruleengine.model.DslRule;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.parser.LoanRulesVisitor;
import com.aishwarya.FinBank.ruleengine.repository.RuleRepository;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
        private DslRuleMapper dslRuleMapper;

    public DslRule processRules(RulesRequestDto rulesRequestDto) {
       DslRule dslRule = toEntity(rulesRequestDto);

    }

    public DslRule toEntity(RulesRequestDto rulesRequestDto) {
        return dslRuleMapper.toEntity(rulesRequestDto);
    }
}
