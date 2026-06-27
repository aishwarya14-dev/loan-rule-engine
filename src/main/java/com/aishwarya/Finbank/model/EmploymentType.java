package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employment_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employment_type")
    private String employmentType;
}
