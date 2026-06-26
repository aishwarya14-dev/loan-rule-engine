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
    @Column(name = "rule_evaluation_score")
    private Double ruleEvaluationScore;
    private String description;
    @ManyToOne
    @JoinColumn(name = "loan_application_id")
    private LoanApplication loanApplication;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_factor_config_id")
    private LoanTypeFactorConfig loanTypeFactorConfig;

    @Builder
    public RuleResult(boolean passed, String message, Double ruleEvaluationScore, LoanApplication loanApplication) {
        this.passed = passed;
        this.message = message;
        this.ruleEvaluationScore = ruleEvaluationScore;
        this.loanApplication = loanApplication;
    }
}
