package com.aishwarya.Finbank.validator;

import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.exceptions.DslValidationException;
import com.aishwarya.Finbank.repository.EmploymentTypeRepo;
import com.aishwarya.Finbank.repository.JobTitleRepo;
import com.aishwarya.Finbank.repository.LoanTypeRepo;
import com.aishwarya.Finbank.repository.RegionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DslSemanticValidator {
    private final LoanFieldAccessorRegistry registry;
    private final RegionRepo regionRepository;
    private final EmploymentTypeRepo employmentTypeRepository;
    private final JobTitleRepo jobTitleRepository;
    private final LoanTypeRepo loanTypeRepository;

    public void validate(LoanRulesParser.StatementContext tree) {
        List<String> errors = new ArrayList<>();
        validateExpression(tree.expression(), errors);  // start from root expression

        if (!errors.isEmpty()) {
            throw new DslValidationException(errors);   // throw all errors together
        }
    }

    private void validateExpression(LoanRulesParser.ExpressionContext ctx, List<String> errors) {
        if (ctx instanceof LoanRulesParser.AndExpressionContext andExpressionContext) {
            validateExpression(andExpressionContext.expression(0), errors);  // validate left
            validateExpression(andExpressionContext.expression(1), errors);
        } else if (ctx instanceof LoanRulesParser.OrExpressionContext orExpressionContext) {
            validateExpression(orExpressionContext.expression(0), errors);  // validate left
            validateExpression(orExpressionContext.expression(1), errors);
        } else if (ctx instanceof LoanRulesParser.ParenExpressionContext parenExpressionContext) {
            validateExpression(parenExpressionContext.expression(), errors);
        } else if (ctx instanceof LoanRulesParser.ConditionExpressionContext conditionExpressionContext) {
            validateCondition(conditionExpressionContext.condition(), errors);
        }

    }

    private void validateCondition(LoanRulesParser.ConditionContext ctx,
                                   List<String> errors) {
        String field = ctx.IDENTIFIER().getText();
        String operator = ctx.operator().getText();
        String rawValue = ctx.value().getText().replace("'", "");

        // field must exist in registry
        if (!registry.containsField(field)) {
            errors.add("Unknown field '" + field + "'. " +
                    "Valid fields: " + registry.getRegisteredFields());
            return;
        }

        // string fields cannot use numeric operators
        if (isStringField(field) && isNumericOperator(operator)) {
            errors.add("Field '" + field + "' is a String. " +
                    "Only == and != are supported, got '" + operator + "'");
        }

        // string values must exist in DB lookup tables
        if (isStringField(field)) {
            validateLookupValue(field, rawValue, errors);
        }
    }

    private void validateLookupValue(String field, String value,
                                     List<String> errors) {
        switch (field) {
            case "region" -> {
                if (!regionRepository.existsByRegionName(value))
                    errors.add("Invalid region '" + value);
            }
            case "employmentType" -> {
                if (!employmentTypeRepository.existsByEmploymentType(value))
                    errors.add("Invalid employmentType '" + value);
            }
            case "jobTitle" -> {
                if (!jobTitleRepository.existsByJobTitle(value))
                    errors.add("Invalid jobTitle '" + value);
            }
            case "loanType" -> {
                if (!loanTypeRepository.existsByLoanType(value))
                    errors.add("Invalid loanType '" + value);
            }
        }
    }

    private boolean isStringField(String field) {
        return Set.of("region", "employmentType",
                "jobTitle", "loanType").contains(field);
    }

    private boolean isNumericOperator(String operator) {
        return Set.of(">", ">=", "<", "<=").contains(operator);
    }
}