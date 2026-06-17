package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@Getter
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
    @NotBlank(message = "Role is mandatory")
    private String role;
    @Column(unique = true, name = "mobile_number")
    @NotBlank(message = "Mobile number is mandatory")
    private String mobileNumber;
}
