package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "factor_evaluation_result")
@Getter
@Setter
@NoArgsConstructor
public class FactorEvaluationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_result_id", nullable = false)
    private LoanApplicationResult result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factor_id", nullable = false)
    private Factor factor;

    private Double factorScore;

    private Double maxFactorScore;

    @CreationTimestamp
    private LocalDateTime evaluatedAt;
}
