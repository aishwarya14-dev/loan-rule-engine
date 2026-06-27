package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "region")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "region_name")
    private String regionName;
}
