package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "job_title")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class JobTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "job_title")
    private String jobTitle;
}
