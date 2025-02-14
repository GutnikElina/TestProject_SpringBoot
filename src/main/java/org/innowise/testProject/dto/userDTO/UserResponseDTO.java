package org.innowise.testProject.dto.userDTO;

import lombok.Builder;
import lombok.Data;
import org.innowise.testProject.model.Role;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private Role role;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
}
