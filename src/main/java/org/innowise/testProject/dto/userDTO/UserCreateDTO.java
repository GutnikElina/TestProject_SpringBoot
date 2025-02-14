package org.innowise.testProject.dto.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 5, message = "Username must be at least 5 characters.")
    private String username;

    private String password;

    @Email(message = "Email must be valid.")
    private String email;

    private String firstName;
    private String lastName;
    private String birthday;
}