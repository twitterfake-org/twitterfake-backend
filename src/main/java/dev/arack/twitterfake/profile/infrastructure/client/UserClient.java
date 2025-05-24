package dev.arack.twitterfake.profile.infrastructure.client;

import dev.arack.twitterfake.iam.infrastructure.dto.response.UserResponse;

public interface UserClient {
    UserResponse getCurrentUser();
}