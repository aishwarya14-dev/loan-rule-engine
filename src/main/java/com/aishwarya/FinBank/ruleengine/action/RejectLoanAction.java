package com.aishwarya.FinBank.ruleengine.action;

import com.aishwarya.FinBank.model.ApplicationStatus;
import com.aishwarya.FinBank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("rejectLoanAction")
public class RejectLoanAction implements RuleAction {
    @Override
    public void applyAction(LoanApplication application) {
       application.setStatus(ApplicationStatus.REJECTED);
    }
}
