package com.aishwarya.FinBank.ruleengine.model;

import com.aishwarya.FinBank.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RuleResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean passed;
    private String message;
    @Column(name = "expected_value", columnDefinition = "TEXT")
    private Object expectedValue;
    private String description;
    @ManyToOne
    @JoinColumn(name = "loan_application_id")
    private LoanApplication loanApplication;

    @Builder
    public RuleResult(boolean passed, String message,Object expectedValue,LoanApplication loanApplication) {
        this.passed = passed;
        this.message = message;
        this.expectedValue = expectedValue;
        this.loanApplication = loanApplication;
    }
}
