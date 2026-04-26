package com.aishwarya.FinBank.ruleengine.action;

import com.aishwarya.FinBank.model.ApplicationStatus;
import com.aishwarya.FinBank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("reviewLoanAction")
public class ReviewLoanAction implements RuleAction {
    @Override
    public void applyAction(LoanApplication application) {
//       application.setStatus(ApplicationStatus.UNDER_REVIEW);
    }
}
