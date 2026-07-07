package com.aishwarya.Finbank.validator;
import com.aishwarya.Finbank.exceptions.DslValidationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.repository.RuleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class DslDuplicateValidator {

    private final RuleRepository dslRuleRepository;
    private final RuleEngineMetrics metrics;

    public void validate(String dslRule,Long loanTypeId) {
        String normalized = dslRule.trim().replaceAll("\\s+", " ");
        boolean exists = dslRuleRepository.existsByDslRuleAndLoanTypeId(
                normalized,
                loanTypeId
        );
        if (exists) {
            log.error("Duplicate rule detected: '{}'", normalized);
            metrics.incrementDuplicateRejected();
            throw new DslValidationException(List.of(
                    "Rule already exists: '" + normalized + "'"
            ));
        }
    }
}
