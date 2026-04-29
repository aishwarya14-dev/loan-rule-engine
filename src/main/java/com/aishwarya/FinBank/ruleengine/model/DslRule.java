package com.aishwarya.FinBank.ruleengine.model;

import com.aishwarya.FinBank.model.LoanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class DslRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dsl_rule", columnDefinition = "TEXT")
    private String dslRule;
    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    private LoanType loanType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
