package org.innowise.testProject.security;

import lombok.RequiredArgsConstructor;
import org.innowise.testProject.dto.userDTO.UserAuthDTO;
import org.innowise.testProject.dto.userDTO.UserCreateDTO;
import org.innowise.testProject.dto.userDTO.UserResponseDTO;
import org.innowise.testProject.mapper.userMapper.UserResponseMapper;
import org.innowise.testProject.model.RefreshToken;
import org.innowise.testProject.model.User;
import org.innowise.testProject.service.CustomUserDetailsService;
import org.innowise.testProject.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomUserDetailsService customUserService;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthenticationResponse signUp(UserCreateDTO request) {
        User newUser = userService.save(request);

        var userDetails = customUserService.loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenService.createRefreshToken(newUser, refreshToken, Instant.now().plusSeconds(60 * 60 * 24 * 7));

        return new JwtAuthenticationResponse(jwt, refreshToken);
    }


    public JwtAuthenticationResponse signIn(UserAuthDTO request) {
        User user = userService.findEntityByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var userDetails = customUserService.loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(userDetails);

        RefreshToken existingToken = refreshTokenService.findValidTokenByUser(user);
        String refreshToken;

        if (existingToken != null) {
            refreshToken = existingToken.getToken();
        } else {
            refreshToken = jwtService.generateRefreshToken(userDetails);
            refreshTokenService.createRefreshToken(user, refreshToken, Instant.now().plusSeconds(60 * 60 * 24 * 7));
        }

        return new JwtAuthenticationResponse(jwt, refreshToken);
    }


    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        var storedToken = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenService.deleteByToken(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        var username = jwtService.extractUserName(refreshToken);
        var userDetails = customUserService.loadUserByUsername(username);

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            var newAccessToken = jwtService.generateToken(userDetails);
            return new JwtAuthenticationResponse(newAccessToken, refreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
