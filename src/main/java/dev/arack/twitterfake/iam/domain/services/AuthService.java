package dev.arack.twitterfake.iam.domain.services;

import dev.arack.twitterfake.iam.infrastructure.dto.request.LoginRequest;
import dev.arack.twitterfake.iam.infrastructure.dto.request.SignupRequest;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import dev.arack.twitterfake.iam.infrastructure.dto.response.AuthResponse;

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
    AuthResponse login(LoginRequest loginRequest);

    /**
     * Registers a new user with the specified details and password.
     *
     * @param signupRequest The {@link UserEntity} object containing user details.
     * @return An {@link AuthResponse} object containing the authentication result.
     */
    AuthResponse signup(SignupRequest signupRequest);

    AuthResponse guest();

    void logout();
}