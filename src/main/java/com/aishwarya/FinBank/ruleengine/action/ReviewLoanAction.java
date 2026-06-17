package com.aishwarya.Finbank.ruleengine.action;

import com.aishwarya.Finbank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("reviewLoanAction")
public class ReviewLoanAction implements RuleAction {
    @Override
    public void applyAction(LoanApplication application) {
//       application.setStatus(ApplicationStatus.UNDER_REVIEW);
    }
}
