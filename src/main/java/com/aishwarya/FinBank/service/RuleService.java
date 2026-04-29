package com.aishwarya.FinBank.service;


import com.aishwarya.FinBank.LoanRulesLexer;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import com.aishwarya.FinBank.mapper.DslRuleMapper;
import com.aishwarya.FinBank.ruleValidator.DslDuplicateValidator;
import com.aishwarya.FinBank.ruleValidator.DslSemanticValidator;
import com.aishwarya.FinBank.ruleValidator.DslSyntaxValidator;
import com.aishwarya.FinBank.ruleengine.dto.mappers.RuleMapper;
import com.aishwarya.FinBank.ruleengine.model.DslRule;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.parser.LoanRulesVisitor;
import com.aishwarya.FinBank.ruleengine.repository.RuleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RuleService {

    private final DslSyntaxValidator syntaxValidator;
    private final DslSemanticValidator semanticValidator;
    private final DslDuplicateValidator duplicateValidator;
    private final RuleRepository dslRuleRepository;

    @Transactional
    public DslRule save(RulesRequestDto dto) {
        String dslText = dto.getRuleText();

        // syntax — parse tree returned for reuse, no double parsing
        LoanRulesParser.StatementContext tree = syntaxValidator.validate(dslText);

        // semantic — uses same tree
        semanticValidator.validate(tree);

        // duplicate — normalized string check
        duplicateValidator.validate(dslText);

        // all passed
        DslRule entity = new DslRule();
        entity.setDslRule(dslText.trim().replaceAll("\\s+", " "));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        return dslRuleRepository.save(entity);
    }
}
