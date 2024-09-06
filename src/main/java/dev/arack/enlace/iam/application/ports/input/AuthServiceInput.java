package dev.arack.enlace.iam.application.ports.input;

import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.LoginRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.RegisterRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.AuthResponse;

public interface AuthServiceInput {
    AuthResponse login(LoginRequest authRequest);
    void register(RegisterRequest authRequest);
}
