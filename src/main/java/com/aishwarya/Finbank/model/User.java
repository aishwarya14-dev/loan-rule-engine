package com.aishwarya.Finbank.model;

import com.aishwarya.Finbank.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity(name = "users")
@Table(name = "users")
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(unique = true, name = "mobile_number")
    @NotBlank(message = "Mobile number is mandatory")
    private String mobileNumber;
}
