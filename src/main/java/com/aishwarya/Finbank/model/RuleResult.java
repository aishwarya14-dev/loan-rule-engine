package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rule_result")
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
    private String expectedValue;
    private String description;
    @ManyToOne
    @JoinColumn(name = "loan_application_id")
    private LoanApplication loanApplication;

    @Builder
    public RuleResult(boolean passed, String message, String expectedValue, LoanApplication loanApplication) {
        this.passed = passed;
        this.message = message;
        this.expectedValue = expectedValue;
        this.loanApplication = loanApplication;
    }
}
