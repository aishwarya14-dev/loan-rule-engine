package com.aishwarya.Finbank.utility;

import com.aishwarya.Finbank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class LoanFieldAccessorRegistry {
    private final Map<String, Function<LoanApplication, Object>> fieldAccessors = new ConcurrentHashMap<>();

    public LoanFieldAccessorRegistry() {
        fieldAccessors.put("creditScore", LoanApplication::getCreditScore);
        fieldAccessors.put("existingLoans", LoanApplication::getExistingLoans);
        fieldAccessors.put("employmentTenure", LoanApplication::getEmploymentTenure);
        fieldAccessors.put("companyRating", LoanApplication::getCompanyRating);

        fieldAccessors.put("loanAmount",
                app -> app.getLoanAmount() != null
                        ? app.getLoanAmount().doubleValue()
                        : null);

        fieldAccessors.put("monthlyIncome",
                app -> app.getMonthlyIncome() != null
                        ? app.getMonthlyIncome().doubleValue()
                        : null);

        fieldAccessors.put("region",
                app -> app.getRegion() != null
                        ? app.getRegion().getRegionName()
                        : null);
        fieldAccessors.put("employmentType",
                app -> app.getEmploymentType() != null
                        ? app.getEmploymentType().getEmploymentType()
                        : null);
    }

    public Function<LoanApplication, Object> getActualValGetterFunction(String fieldName) {
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
