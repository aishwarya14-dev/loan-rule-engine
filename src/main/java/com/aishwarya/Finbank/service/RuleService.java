package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.dto.rules.dynamicrules.RulesRequestDto;
import com.aishwarya.Finbank.enums.RuleSeverity;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.model.Factor;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.repository.FactorRepo;
import com.aishwarya.Finbank.repository.LoanTypeRepo;
import com.aishwarya.Finbank.repository.RuleRepository;
import com.aishwarya.Finbank.ruleengine.loader.DynamicRuleLoader;
import com.aishwarya.Finbank.validator.DslDuplicateValidator;
import com.aishwarya.Finbank.validator.DslSemanticValidator;
import com.aishwarya.Finbank.validator.DslSyntaxValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@AllArgsConstructor
public class RuleService {

    private final DslSyntaxValidator syntaxValidator;
    private final DslSemanticValidator semanticValidator;
    private final DslDuplicateValidator duplicateValidator;
    private final RuleRepository dslRuleRepository;
    private final DynamicRuleLoader dynamicRuleLoader;
    private final LoanTypeRepo loanTypeRepo;
    private final FactorRepo factorRepo;
    private final RuleEngineMetrics metrics;

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
        Factor factor = getFactorById(dto.getFactorId());
        entity.setEvidenceWeight(dto.getEvidenceWeight());
        entity.setFactor(factor);
        if(dto.getRuleSeverity() != null)
            entity.setRuleSeverity(dto.getRuleSeverity());
        else
            entity.setRuleSeverity(RuleSeverity.NORMAL);

        LoanType loanType = loanTypeRepo.findById(dto.getLoanTypeId())
                .orElseThrow(() -> {
                    log.error("LoanType not found for id: {}", dto.getLoanTypeId());
                    return new IllegalArgumentException(
                            "LoanType not found for id: " + dto.getLoanTypeId()
                    );
                });
        entity.setLoanType(loanType);

        // save rule to the db
        DslRule savedRule = dslRuleRepository.save(entity);
        metrics.incrementRuleCreated();
        // evict cache
        evictByLoanType(loanType);
        return savedRule;
    }

    private void evictByLoanType(LoanType loanType) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            dynamicRuleLoader.evictByLoanType(loanType);
                            metrics.incrementCacheEviction();
                        } catch (Exception ex) {
                            log.error("Error evicting rules for loan type: {}", loanType.getId(), ex);
                        }
                    }
                }
        );
    }

    private Factor getFactorById(Integer id){
        return factorRepo.findById(id.longValue())
                .orElseThrow(() -> {
                    log.error("Factor not found for id: {}", id);
                    return new EntityNotFoundException("Factor not found for id: " + id);
                });
    }
}
