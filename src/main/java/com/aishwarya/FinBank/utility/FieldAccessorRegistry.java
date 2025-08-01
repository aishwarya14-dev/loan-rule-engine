package com.aishwarya.FinBank.utility;

import com.aishwarya.FinBank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class FieldAccessorRegistry {
    Map<String, Function<LoanApplication,Object>> fieldAccessors = new HashMap<>();

    public FieldAccessorRegistry(){
        fieldAccessors.put("creditScore",LoanApplication :: getCreditScore);
        fieldAccessors.put("amount",LoanApplication :: getAmount);
        fieldAccessors.put("monthlyIncome",LoanApplication :: getMonthlyIncome);
        fieldAccessors.put("existingLoans",LoanApplication :: getExistingLoans);
        fieldAccessors.put("employmentTenure",LoanApplication :: getEmploymentTenure);
        fieldAccessors.put("companyRating",LoanApplication :: getCompanyRating);
        fieldAccessors.put("region",LoanApplication :: getApplicantRegion);
        fieldAccessors.put("employmentType",LoanApplication :: getEmploymentType);
        fieldAccessors.put("existingLoans",LoanApplication :: getExistingLoans);
    }

    public Function<LoanApplication, Object> getAccessor(String fieldName) {
        return fieldAccessors.get(fieldName);
    }

    public boolean containsField(String fieldName) {
        return fieldAccessors.containsKey(fieldName);
    }
}
