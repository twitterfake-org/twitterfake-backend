package dev.arack.twitterfake.profile.infrastructure.dto.response;

import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;

import java.util.List;

public record UserResponse (
        Long id,
        String username,
        List<String> role
) {
    public static UserResponse of(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().map(role -> role.getRoleName().name()).toList()
        );
    }
}