package com.aishwarya.FinBank.service;
import com.aishwarya.FinBank.LoanRulesParser;
import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import com.aishwarya.FinBank.ruleValidator.DslDuplicateValidator;
import com.aishwarya.FinBank.ruleValidator.DslSemanticValidator;
import com.aishwarya.FinBank.ruleValidator.DslSyntaxValidator;
import com.aishwarya.FinBank.model.DslRule;
import com.aishwarya.FinBank.repository.RuleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
