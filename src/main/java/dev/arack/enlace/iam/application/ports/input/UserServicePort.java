package dev.arack.enlace.iam.application.ports.input;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;

import java.util.List;

/**
 * Interface for user management services, providing methods to retrieve, update, and delete user information.
 */
public interface UserServicePort {
    /**
     * Retrieves a list of all users.
     *
     * @return A {@link List} of {@link UserEntity} objects representing all users.
     */
    List<UserEntity> getAllUsers();

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to be retrieved.
     * @return A {@link UserEntity} object representing the user with the specified username.
     */
    UserEntity getUserByUsername(String username);

    /**
     * Updates the information of a user identified by their username.
     *
     * @param userId The id of the user to be updated.
     * @param userRequest The {@link UserRequest} object containing the new user information.
     */
    void updateUser(Long userId, UserRequest userRequest);

    /**
     * Deletes a user identified by their username.
     *
     * @param userId The id of the user to be deleted.
     */
    void deleteUser(Long userId);
}
