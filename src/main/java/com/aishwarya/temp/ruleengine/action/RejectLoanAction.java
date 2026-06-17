package com.aishwarya.Finbank.ruleengine.action;

import com.aishwarya.Finbank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("rejectLoanAction")
public class RejectLoanAction implements RuleAction {
    @Override
    public void applyAction(LoanApplication application) {
//       application.upda(ApplicationStatus.REJECTED);
    }
}
