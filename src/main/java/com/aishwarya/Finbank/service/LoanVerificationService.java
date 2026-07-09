package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.model.LoanApplication;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class LoanVerificationService {

    // mocking the verification process
    public void implementVerification(LoanApplication loanApplication){
        calculateLoanToValueRatio(loanApplication);
        checkKycVerification(loanApplication);
        checkPanVerification(loanApplication);
        checkAadhaarVerification(loanApplication);
        checkPropertyVerification(loanApplication);
        checkFraudulence(loanApplication);
        checkBlacklisting(loanApplication);
        verifyIfExistingCustomer(loanApplication);
        checkAverageAccountBalance(loanApplication);
        hasFixedDeposit(loanApplication);
        checkGuarantorPresent(loanApplication);
        debtToIncomeRatio(loanApplication);
        checkCompanyRating(loanApplication);
        checkLoanDefaults(loanApplication);
        checkIncomeVerified(loanApplication);
    }

    private void calculateLoanToValueRatio(LoanApplication application){
        BigDecimal propertyValue = application.getPropertyValue();
        Double loanToValueRatio =
                application.getLoanAmount().divide(propertyValue, MathContext.DECIMAL64)
                        .multiply(BigDecimal.valueOf(100)).doubleValue();
        application.updateLoanToValueRatio(loanToValueRatio);
    }

    private void checkKycVerification(LoanApplication application){
        application.updateKycVerified(true);
    }

    private void checkPanVerification(LoanApplication application){
        application.updatePanVerifiied(true);
    }

    private void checkAadhaarVerification(LoanApplication application){
        application.updateAadharVerified(true);
    }

    private void checkPropertyVerification(LoanApplication application){
        application.updatePropertyVerified(true);
    }

    private void checkFraudulence(LoanApplication application){
        application.updateFraudFlag(false);
    }

    private void checkBlacklisting(LoanApplication application){
        application.updateBlacklistingStatus(false);
    }

    private void verifyIfExistingCustomer(LoanApplication application){
        if (application.getUser().getCreatedAt().isBefore(LocalDate.now().minusYears(1).atStartOfDay())) {
            application.updateExistingCustomer(true);
        } else {
            application.updateExistingCustomer(false);
        }
    }

    private void checkAverageAccountBalance(LoanApplication application){
        BigDecimal averageBalance =
                application.getMonthlyIncome()
                        .multiply(BigDecimal.valueOf(0.35));

        application.updateAverageAccountBalance(averageBalance);
    }

    private void hasFixedDeposit(LoanApplication application){
        application.updateHasFixedDeposit(true);
    }

    private void checkIncomeVerified(LoanApplication application){
        application.updateIncomeVerified(true);
    }

    private void checkGuarantorPresent(LoanApplication application){
        application.UpdateGuarantorsVerified(!application.getGuarantors().isEmpty());
    }

    private void debtToIncomeRatio(LoanApplication application){
        BigDecimal monthlyIncome = application.getOtherMonthlyIncome();
        BigDecimal monthlyEmi = application.getMonthlyEmi();
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Monthly income must be greater than zero");
        }

        if (monthlyEmi == null) {
            throw new IllegalStateException("Monthly EMI cannot be null");
        }

        Double debtToIncomeRatio =
                monthlyEmi
                        .multiply(BigDecimal.valueOf(100))
                        .divide(monthlyIncome, 2, RoundingMode.HALF_UP)
                        .doubleValue();
        application.updateDebtToIncomeRatio(debtToIncomeRatio);
    }

    private void checkCompanyRating(LoanApplication application){
        int companyRating = ThreadLocalRandom.current().nextInt(1, 6);
        application.updateCompanyRating(companyRating);
    }

    private void checkLoanDefaults(LoanApplication application){
        int loanDefaults = ThreadLocalRandom.current().nextInt(1, 7);
        application.updateLoanDefaults(loanDefaults);
    }
}
