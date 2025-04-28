package dev.arack.twitterfake.profile.application.port.output.client;

import dev.arack.twitterfake.iam.application.dto.response.UserResponse;

public interface UserClient {
    UserResponse getCurrentUser();
}