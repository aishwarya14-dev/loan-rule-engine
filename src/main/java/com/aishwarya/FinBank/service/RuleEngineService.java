package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.model.LoanApplication;

public interface RuleEngineService {
    public void evaluateLoanApplication(LoanApplication application, String mode);
}
