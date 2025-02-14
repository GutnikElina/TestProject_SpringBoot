package org.innowise.testProject.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.innowise.testProject.dto.userDTO.UserCreateDTO;
import org.innowise.testProject.dto.userDTO.UserResponseDTO;
import org.innowise.testProject.exception.EntityAlreadyExistException;
import org.innowise.testProject.exception.NoDataFoundException;
import org.innowise.testProject.mapper.userMapper.UserCreateMapper;
import org.innowise.testProject.mapper.userMapper.UserResponseMapper;
import org.innowise.testProject.model.Role;
import org.innowise.testProject.model.User;
import org.innowise.testProject.repository.UserRepository;
import org.innowise.testProject.service.UserService;
import org.innowise.testProject.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(@Valid UserCreateDTO userCreateDTO) {
        userRepository.findByUsername(userCreateDTO.getUsername()).ifPresent(user -> {
            throw new EntityAlreadyExistException("Username already exists.");
        });

        User user = UserCreateMapper.INSTANCE.toEntity(userCreateDTO);
        user.setPassword(PasswordUtil.hashPassword(userCreateDTO.getPassword()));
        user.setRole(Role.ROLE_USER);
        log.info("User '{}' successfully added.", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateProfile(String username, @Valid UserCreateDTO userCreateDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NoDataFoundException("User not found: " + username));

        if (!username.equals(userCreateDTO.getUsername()) && userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
            throw new EntityAlreadyExistException("Username is already taken.");
        }

        updateUserDetails(user, userCreateDTO);
        userRepository.save(user);
        log.info("Profile updated for user '{}'.", username);
    }

    @Override
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoDataFoundException("User not found: " + username));
    }

    @Override
    public UserResponseDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserResponseMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new NoDataFoundException("User not found: " + username));
    }

    private void updateUserDetails(User user, UserCreateDTO dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthday(dto.getBirthday());
        if (StringUtils.isNotBlank(dto.getPassword())) {
            user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        }
    }
}