package dev.arack.enlace.iam.application.ports.input;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.LoginRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.AuthResponse;

/**
 * Interface for authentication services, providing methods for user login and registration.
 */
public interface AuthServicePort {
    /**
     * Logs in a user with the provided credentials.
     *
     * @param authRequest The login request containing user credentials.
     * @return An {@link AuthResponse} object containing the authentication result.
     */
    AuthResponse login(LoginRequest authRequest);

    /**
     * Registers a new user with the specified details and password.
     *
     * @param userEntity The {@link UserEntity} object containing user details.
     * @param password The password for the new user.
     */
    void register(UserEntity userEntity, String password);
}
