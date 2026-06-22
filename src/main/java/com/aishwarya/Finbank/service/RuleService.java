package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.dto.rules.dynamicrules.RulesRequestDto;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.repository.LoanTypeRepo;
import com.aishwarya.Finbank.repository.RuleRepository;
import com.aishwarya.Finbank.ruleengine.loader.DynamicRuleLoader;
import com.aishwarya.Finbank.validator.DslDuplicateValidator;
import com.aishwarya.Finbank.validator.DslSemanticValidator;
import com.aishwarya.Finbank.validator.DslSyntaxValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RuleService {

    private final DslSyntaxValidator syntaxValidator;
    private final DslSemanticValidator semanticValidator;
    private final DslDuplicateValidator duplicateValidator;
    private final RuleRepository dslRuleRepository;
    private final DynamicRuleLoader dynamicRuleLoader;
    private final LoanTypeRepo loanTypeRepo;

    @Transactional
    public DslRule save(RulesRequestDto dto) {
        String dslText = dto.getRuleText();

        // syntax — parse tree returned for reuse, no double parsing
        LoanRulesParser.StatementContext tree = syntaxValidator.validate(dslText);

        // semantic — uses same tree
        semanticValidator.validate(tree);

        // duplicate — normalized string check
        duplicateValidator.validate(dslText);

        // create entity
        DslRule entity = new DslRule();
        entity.setDslRule(dslText.trim().replaceAll("\\s+", " "));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        LoanType loanType = loanTypeRepo.findById(dto.getLoanTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "LoanType not found for id: " + dto.getLoanTypeId()
                ));
        entity.setLoanType(loanType);

        // save rule to the db
        DslRule savedRule = dslRuleRepository.save(entity);
        // evict cache
        evictByLoanType(loanType);
        return savedRule;
    }

    public void evictByLoanType(LoanType loanType) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            dynamicRuleLoader.evictByLoanType(loanType);

                        } catch (Exception ex) {
                            //add log
                        }
                    }
                }
        );
    }
}
