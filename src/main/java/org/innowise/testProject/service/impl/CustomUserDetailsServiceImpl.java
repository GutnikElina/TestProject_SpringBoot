package org.innowise.testProject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.innowise.testProject.model.User;
import org.innowise.testProject.repository.UserRepository;
import org.innowise.testProject.service.CustomUserDetailsService;
import org.innowise.testProject.wrapper.UserWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl
        implements UserDetailsService, CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" +
                        username + "' not found!"));

        log.info("Loaded user: {} with role: {}", user.getUsername(), user.getRole());
        return new UserWrapper(user);
    }
}
