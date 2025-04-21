package dev.arack.enlace.profile.application.port.output.client;

import dev.arack.enlace.iam.application.dto.response.UserResponse;

public interface UserClient {
    UserResponse getCurrentUser();
}