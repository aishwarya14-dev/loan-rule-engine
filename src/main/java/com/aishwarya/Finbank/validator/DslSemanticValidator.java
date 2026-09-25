package com.aishwarya.Finbank.validator;

import com.aishwarya.Finbank.LoanRulesParser;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.repository.*;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.exceptions.DslValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class DslSemanticValidator {
    private final LoanFieldAccessorRegistry registry;
    private final RegionRepo regionRepository;
    private final EmploymentTypeRepo employmentTypeRepository;
    private final JobTitleRepo jobTitleRepository;
    private final LoanTypeRepo loanTypeRepository;
    private final RuleEngineMetrics metrics;
    private final IndustryRepo industryRepo;
    private final PropertyTypeRepo propertyTypeRepo;
    private final LoanPurposeRepo loanPurposeRepo;

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
            log.error("Unknown field '{}' in DSL rule", field);
            metrics.incrementSemanticFailed();
        }

        // string fields cannot use numeric operators
        if (isStringField(field) && isNumericOperator(operator)) {
            errors.add("Field '" + field + "' is a String. " +
                    "Only == and != are supported, got '" + operator + "'");
            log.error("Invalid operator '{}' for String field '{}'", operator, field);
            metrics.incrementSemanticFailed();
        }

        // string fields cannot have boolean value
        if(isStringField(field) && isBooleanValue(rawValue)){
            errors.add("Field '" + field + "' is a String. " );
            log.error("Invalid value '{}' for String field '{}'", rawValue, field);
            metrics.incrementSemanticFailed();
        }

        // boolean fields cannot use numeric operators
        if(isBooleanField(field) && isNumericOperator(operator)){
            errors.add("Field '" + field + "' is a Boolean. " +
                    "Only == and != are supported, got '" + operator + "'");
            log.error("Invalid operator '{}' for Boolean field '{}'", operator, field);
            metrics.incrementSemanticFailed();
        }

        // boolean fields cannot have numeric value
        if(isBooleanField(field) && isNumericValue(rawValue)){
            errors.add("Field '" + field + "' is a Boolean. " +
                    "Only == and != are supported ");
            log.error("Invalid value '{}' for Boolean field '{}'", rawValue, field);
            metrics.incrementSemanticFailed();
        }

        // numeric fields cannot have boolean values
        if(isNumericField(field) && isBooleanValue(rawValue)){
            errors.add("Field '" + field + "' is Numeric. " );
            log.error("Invalid value '{}' for Numeric field '{}'", rawValue, field);
            metrics.incrementSemanticFailed();
        }

        // numeric fields cannot have string value
        if(isNumericField(field) && rawValue.matches("[a-zA-Z]+")){
            errors.add("Field '" + field + "' is Numeric. ");
            log.error("Invalid value '{}' for Numeric field '{}'", rawValue, field);
            metrics.incrementSemanticFailed();
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
                if (!regionRepository.existsByRegionName(value)){
                    errors.add("Invalid region '" + value);
                    log.error("Invalid region '{}' in DSL rule", value);
                    metrics.incrementSemanticFailed();
                }
            }
            case "employmentType" -> {
                if (!employmentTypeRepository.existsByEmploymentType(value)){
                    errors.add("Invalid employmentType '" + value);
                    log.error("Invalid employmentType '{}' in DSL rule", value);
                    metrics.incrementSemanticFailed();
                }

            }
            case "jobTitle" -> {
                if (!jobTitleRepository.existsByJobTitle(value)){
                    errors.add("Invalid jobTitle '" + value);
                    log.error("Invalid jobTitle '{}' in DSL rule", value);
                    metrics.incrementSemanticFailed();
                }
            }
            case "loanType" -> {
                if (!loanTypeRepository.existsByLoanType(value)){
                    errors.add("Invalid loanType '" + value);
                    log.error("Invalid loanType '{}' in DSL rule", value);
                    metrics.incrementSemanticFailed();
                }
            }
            case "industry" -> {
                if (!industryRepo.existsByName(value)){
                    errors.add("Invalid industry '" + value);
                    log.error("Invalid industry '{}' in DSL rule", value);
                    metrics.incrementSemanticFailed();
                }
            }
            case "propertyType" -> {
                if (!propertyTypeRepo.existsByName(value)){
                    errors.add("Invalid property type '" + value);
                    log.error("Invalid property type '{}' in DSL rule", value);
                    metrics.incrementSemanticFailed();
                }
            }
            case "loanPurpose" -> {
                if (!loanPurposeRepo.existsByName(value)){
                    errors.add("Invalid loan purpose '" + value);
                    log.error("Invalid loan purpose '{}' in DSL rule", value);
                    metrics.incrementSemanticFailed();
                }
            }
        }
    }

    private boolean isStringField(String field) {
        return Set.of("region", "employmentType",
                "jobTitle", "loanType","industry","propertyType","loanPurpose").contains(field);
    }

    private boolean isBooleanField(String field) {
        return Set.of("incomeVerified", "incomeTaxReturnAvailable",
                "salaryAccountWithBank", "probationCompleted", "propertyVerified", "existingCustomer", "hasFixedDeposit","ownsHouse",
                "guarantorPresent", "kycVerified", "panVerified", "aadhaarVerified", "fraudFlag", "blacklisted"
                ).contains(field);
    }

    private boolean isNumericField(String field) {
        return Set.of("creditScore", "existingLoans",
                "employmentTenure", "age", "creditHistoryYears", "creditCardUtilization","bankruptcies",
                "annualIncome", "loanAmount", "monthlyIncome", "interestRate", "loanTenureMonths", "otherMonthlyIncome",
                "downPayment" , "companyRating", "monthlyEmi", "missedPaymentsLast12Months", "loanDefaults","propertyValue",
                "propertyAge","averageAccountBalance","residenceYears","totalOutstandingDebt"
        ).contains(field);
    }

    private boolean isNumericOperator(String operator) {
        return Set.of(">", ">=", "<", "<=").contains(operator);
    }

    private boolean isBooleanValue(String value) {
        return Boolean.parseBoolean(value);
    }

    public static boolean isNumericValue(String str) {
        if (str == null || str.isEmpty())
            return false;
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}