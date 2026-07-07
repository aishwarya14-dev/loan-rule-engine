package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.model.LoanApplication;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
@AllArgsConstructor
public class LoanVerificationService {

    public void implementVerification(LoanApplication loanApplication){
        calculateLoanToValueRatio(loanApplication);
    }

    private void calculateLoanToValueRatio(LoanApplication application){
        BigDecimal propertyValue = application.getPropertyValue();
        Double loanToValueRatio =
                application.getLoanAmount().divide(propertyValue, MathContext.DECIMAL64)
                        .multiply(BigDecimal.valueOf(100)).doubleValue();
        application.updateLoanToValueRatio(loanToValueRatio);
    }

    private void checkKycVerification(LoanApplication application){

    }

    private void checkPanVerification(LoanApplication application){

    }

    private void checkAadhaarVerification(LoanApplication application){

    }

    private void checkPropertyVerification(LoanApplication application){

    }

    private void checkFraudulence(LoanApplication application){

    }

    private void checkBlacklisting(LoanApplication application){

    }

    private void verifyIfExistingCustomer(LoanApplication application){

    }

    private void checkAverageAccountBalance(LoanApplication application){

    }

    private void hasFixedDeposit(LoanApplication application){

    }

    private void checkGuarantorPresent(LoanApplication application){
        application.UpdateGuarantorsVerified(!application.getGuarantors().isEmpty());
    }

    private void debtToIncomeRatio(LoanApplication application){

    }

    private void checkCompanyRating(LoanApplication application){

    }

    private void checkLoanDefaults(LoanApplication application){

    }
}
