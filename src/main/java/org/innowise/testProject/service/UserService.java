package org.innowise.testProject.service;

import org.innowise.testProject.dto.userDTO.UserResponseDTO;
import org.innowise.testProject.dto.userDTO.UserCreateDTO;
import org.innowise.testProject.model.User;

/**
 * Service interface for managing users.
 */
public interface UserService {
    /**
     * Creates a new user.
     *
     * @param userCreateDTO the DTO containing user details.
     */
    User save(UserCreateDTO userCreateDTO);

    /**
     * Updates the profile of an existing user.
     *
     * @param username the ID of the user to update.
     * @param userCreateDTO the DTO containing updated profile details.
     */
    void updateProfile(String username, UserCreateDTO userCreateDTO);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user.
     * @return containing the {@link UserResponseDTO}, if found.
     */
    UserResponseDTO findByUsername(String username);

    User findEntityByUsername(String username);
}
