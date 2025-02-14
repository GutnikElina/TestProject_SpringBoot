package org.innowise.testProject.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.innowise.testProject.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    private final JwtService jwtService;
    private final CustomUserDetailsService userService;

    @Setter
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);
        if (StringUtils.isEmpty(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtService.extractUserName(jwt);
            if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("User authenticated successfully: {}", username);
                }
            }
        } catch (ExpiredJwtException e) {
            log.error("JWT expired, checking for refresh token");

            String refreshToken = request.getHeader("Refresh-Token");
            if (StringUtils.isNotEmpty(refreshToken)) {
                try {
                    JwtAuthenticationResponse responseToken = authenticationService.refreshToken(refreshToken);
                    response.setHeader("Authorization", "Bearer " + responseToken.getToken());
                } catch (Exception ex) {
                    log.error("Invalid or expired refresh token", ex);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                log.error("No refresh token provided, JWT expired.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER_NAME);

        if (StringUtils.isNotEmpty(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}