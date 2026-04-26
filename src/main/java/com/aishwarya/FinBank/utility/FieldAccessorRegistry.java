package com.aishwarya.FinBank.utility;

import com.aishwarya.FinBank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class FieldAccessorRegistry {
    private final Map<String, Function<LoanApplication,Object>> fieldAccessors = new ConcurrentHashMap<>();

    public FieldAccessorRegistry(){
        fieldAccessors.put("creditScore",LoanApplication :: getCreditScore);
        fieldAccessors.put("loanAmount",LoanApplication :: getLoanAmount);
        fieldAccessors.put("monthlyIncome",LoanApplication :: getMonthlyIncome);
        fieldAccessors.put("existingLoans",LoanApplication :: getExistingLoans);
        fieldAccessors.put("employmentTenure",LoanApplication :: getEmploymentTenure);
        fieldAccessors.put("companyRating",LoanApplication :: getCompanyRating);
        fieldAccessors.put("region",LoanApplication :: getRegion);
        fieldAccessors.put("employmentType",LoanApplication :: getEmploymentType);
    }

    public Function<LoanApplication, Object> getAccessor(String fieldName) {
        Function<LoanApplication, Object> accessor = fieldAccessors.get(fieldName);
        if (accessor == null) {
            throw new IllegalArgumentException(
                    "Unknown field '" + fieldName + "' in rule. " +
                            "Registered fields are: " + fieldAccessors.keySet()
            );
        }
        return accessor;
    }

    public boolean containsField(String fieldName) {
        return fieldAccessors.containsKey(fieldName);
    }

    public Set<String> getRegisteredFields() {
        return Collections.unmodifiableSet(fieldAccessors.keySet());
    }
}
