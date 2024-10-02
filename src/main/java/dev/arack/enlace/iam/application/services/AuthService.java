package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.dto.request.SignupRequest;
import dev.arack.enlace.iam.application.dto.request.UserRequest;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.application.dto.request.LoginRequest;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;

/**
 * Interface for authentication services, providing methods for user login and registration.
 */
public interface AuthService {
    /**
     * Logs in a user with the provided credentials.
     *
     * @param loginRequest The login request containing user credentials.
     * @return An {@link AuthResponse} object containing the authentication result.
     */
    AuthResponse login(UserRequest loginRequest);

    /**
     * Registers a new user with the specified details and password.
     *
     * @param loginRequest The {@link UserEntity} object containing user details.
     * @return An {@link AuthResponse} object containing the authentication result.
     */
    AuthResponse signup(UserRequest loginRequest);
}