package com.aishwarya.FinBank.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name="users")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String password;
    private String role;
    @Column(unique = true, name = "mobile_number")
    private String mobileNumber;
}
