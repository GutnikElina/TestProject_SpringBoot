package org.innowise.testProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be null or empty.")
    @Size(min = 5, message = "Username must be at least 5 characters long.")
    @Size(max = 50, message = "Username cannot be longer than 50 characters.")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password cannot be null or empty.")
    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String firstName;
    private String lastName;

    @Email(message = "Email must be valid.")
    private String email;

    private String birthday;
}
