package com.aishwarya.FinBank.ruleValidator;

import com.aishwarya.FinBank.exceptions.DslValidationException;
import com.aishwarya.FinBank.repository.RuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DslDuplicateValidator {

    private final RuleRepository dslRuleRepository;

    public void validate(String dslRule) {
        String normalized = dslRule.trim().replaceAll("\\s+", " ");

        if (dslRuleRepository.existsByDslRule(normalized)) {
            throw new DslValidationException(List.of(
                    "Rule already exists: '" + normalized + "'"
            ));
        }
    }
}
