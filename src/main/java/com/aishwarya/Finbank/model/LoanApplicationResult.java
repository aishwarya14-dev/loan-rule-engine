package com.aishwarya.Finbank.model;

import com.aishwarya.Finbank.enums.Decision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_application_result")
@Getter
@Setter
@NoArgsConstructor
public class LoanApplicationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication application;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    private Double finalScore;

    @CreationTimestamp
    private LocalDateTime evaluatedAt;
}
