package org.innowise.testProject.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;
}
