package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "employment_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmploymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employment_type")
    private String employmentType;
}
