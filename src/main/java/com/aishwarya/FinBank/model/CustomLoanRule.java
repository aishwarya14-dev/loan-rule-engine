package com.aishwarya.FinBank.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="rule")
public class CustomLoanRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String dslRule;

    public String getRuleText() {
        return dslRule;
    }
}
