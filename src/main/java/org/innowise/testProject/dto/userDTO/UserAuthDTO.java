package org.innowise.testProject.dto.userDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthDTO {
    private String username;
    private String password;
}
