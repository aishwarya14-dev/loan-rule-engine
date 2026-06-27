package com.aishwarya.Finbank.model;

import com.aishwarya.Finbank.enums.RuleSeverity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "dsl_rule")
@AllArgsConstructor
@NoArgsConstructor
public class DslRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dsl_rule", columnDefinition = "TEXT")
    private String dslRule;
    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    private LoanType loanType;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factor_id")
    private Factor factor;
    // strength of the rule used to determine the importance / weight of the rule in decision making (range from 0.0 to 1.0)
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @Column(name = "evidence_weight")
    private Double evidenceWeight;
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private RuleSeverity ruleSeverity = RuleSeverity.NORMAL;
}
