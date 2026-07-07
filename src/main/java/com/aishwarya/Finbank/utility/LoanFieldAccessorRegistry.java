package com.aishwarya.Finbank.utility;

import com.aishwarya.Finbank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public final class LoanFieldAccessorRegistry {
    private final Map<String, Function<LoanApplication, Object>> fieldAccessors = new ConcurrentHashMap<>();

    public LoanFieldAccessorRegistry() {
        fieldAccessors.put("creditScore", LoanApplication::getCreditScore);
        fieldAccessors.put("existingLoans", LoanApplication::getExistingLoans);
        fieldAccessors.put("employmentTenure", LoanApplication::getEmploymentTenure);
        fieldAccessors.put("companyRating", LoanApplication::getCompanyRating);
        fieldAccessors.put("age", LoanApplication::getAge);
        fieldAccessors.put("jobTitle", LoanApplication :: getJobTitle);
        fieldAccessors.put("creditHistoryYears",
                     app -> app.getCreditHistoryYears() != null ?
                             app.getCreditHistoryYears().intValue()
                             : null
                );
        fieldAccessors.put("creditCardUtilization",
                app -> app.getCreditCardUtilization() != null ?
                        app.getCreditCardUtilization().doubleValue()
                        : null
                );
        fieldAccessors.put("bankruptcies",
                app -> app.getBankruptcies() != null ?
                        app.getBankruptcies().intValue()
                        : null
        );
        fieldAccessors.put("annualIncome",
                app -> app.getAnnualIncome() != null ?
                        app.getAnnualIncome().doubleValue()
                        : null
        );
        fieldAccessors.put("loanAmount",
                app -> app.getLoanAmount() != null
                        ? app.getLoanAmount().doubleValue()
                        : null);

        fieldAccessors.put("monthlyIncome",
                app -> app.getMonthlyIncome() != null
                        ? app.getMonthlyIncome().doubleValue()
                        : null);

        fieldAccessors.put("otherMonthlyIncome",
                app -> app.getOtherMonthlyIncome() != null ?
                        app.getOtherMonthlyIncome().doubleValue()
                        : null
        );

        fieldAccessors.put("incomeVerified",
                app -> app.getIncomeVerified() != null ?
                        app.getIncomeVerified().booleanValue()
                        : null
        );

        fieldAccessors.put("incomeTaxReturnAvailable",
                app -> app.getIncomeTaxReturnAvailable() != null ?
                        app.getIncomeTaxReturnAvailable().booleanValue()
                        : null
        );

        fieldAccessors.put("region",
                app -> app.getRegion() != null
                        ? app.getRegion().getRegionName()
                        : null);
        fieldAccessors.put("employmentType",
                app -> app.getEmploymentType() != null
                        ? app.getEmploymentType().getEmploymentType()
                        : null);

        fieldAccessors.put("employerName",
                app -> app.getEmployerName() != null
                        ? app.getEmployerName()
                        : null);

        fieldAccessors.put("companyRating",
                app -> app.getCompanyRating() != null
                        ? app.getCompanyRating().intValue()
                        : null);

        fieldAccessors.put("probationCompleted",
                app -> app.getProbationCompleted() != null
                        ? app.getProbationCompleted().booleanValue()
                        : null);

        fieldAccessors.put("industry",
                app -> app.getIndustry() != null
                        ? app.getIndustry()
                        : null);
        fieldAccessors.put("monthlyEmi",
                app -> app.getMonthlyEmi() != null
                        ? app.getMonthlyEmi().doubleValue()
                        : null);

        fieldAccessors.put("debtToIncomeRatio",
                app -> app.getDebtToIncomeRatio() != null
                        ? app.getDebtToIncomeRatio().doubleValue()
                        : null);
        fieldAccessors.put("loanDefaults",
                app -> app.getLoanDefaults() != null
                        ? app.getLoanDefaults().intValue()
                        : null);
        fieldAccessors.put("propertyType",
                app -> app.getPropertyType() != null
                        ? app.getPropertyType()
                        : null);
        fieldAccessors.put("propertyValue",
                app -> app.getPropertyValue() != null
                        ? app.getPropertyValue().doubleValue()
                        : null);
        fieldAccessors.put("propertyAge",
                app -> app.getPropertyAge() != null
                        ? app.getPropertyAge().intValue()
                        : null);
        fieldAccessors.put("propertyVerified",
                app -> app.getPropertyVerified() != null
                        ? app.getPropertyVerified().booleanValue()
                        : null);
        fieldAccessors.put("existingCustomer",
                app -> app.getExistingCustomer() != null
                        ? app.getExistingCustomer().booleanValue()
                        : null);
//        fieldAccessors.put("customerSince",
//                app -> app.getCustomerSince()!= null
//                        ? app.getCustomerSince()
//                        : null);
        fieldAccessors.put("averageAccountBalance",
                app -> app.getAverageAccountBalance() != null
                        ? app.getAverageAccountBalance().doubleValue()
                        : null);
        fieldAccessors.put("hasFixedDeposit",
                app -> app.getHasFixedDeposit() != null
                        ? app.getHasFixedDeposit().booleanValue()
                        : null);
        fieldAccessors.put("residenceYears",
                app -> app.getResidenceYears() != null
                        ? app.getResidenceYears().intValue()
                        : null);
        fieldAccessors.put("ownsHouse",
                app -> app.getOwnsHouse() != null
                        ? app.getOwnsHouse().booleanValue()
                        : null);
        fieldAccessors.put("loanToValueRatio",
                app -> app.getLoanToValueRatio() != null
                        ? app.getLoanToValueRatio().doubleValue()
                        : null);
        fieldAccessors.put("guarantorPresent",
                app -> app.getGuarantorPresent() != null
                        ? app.getGuarantorPresent().booleanValue()
                        : null);
        fieldAccessors.put("kycVerified",
                app -> app.getKycVerified() != null
                        ? app.getKycVerified().booleanValue()
                        : null);
        fieldAccessors.put("panVerified",
                app -> app.getPanVerified() != null
                        ? app.getPanVerified().booleanValue()
                        : null);
        fieldAccessors.put("aadhaarVerified",
                app -> app.getAadhaarVerified() != null
                        ? app.getAadhaarVerified().booleanValue()
                        : null);
        fieldAccessors.put("fraudFlag",
                app -> app.getFraudFlag() != null
                        ? app.getFraudFlag().booleanValue()
                        : null);
        fieldAccessors.put("blacklisted",
                app -> app.getBlacklisted() != null
                        ? app.getBlacklisted().booleanValue()
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
