package dev.arack.enlace.profile.application.dto.response;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;

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