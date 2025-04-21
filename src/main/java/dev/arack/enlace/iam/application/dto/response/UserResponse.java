package dev.arack.enlace.iam.application.dto.response;


import dev.arack.enlace.iam.domain.aggregates.UserEntity;

import java.util.List;

public record UserResponse (
    Long id,
    String fullName,
    String username,
    List<String> role
) {
    public static UserResponse fromEntity(UserEntity user) {
        return new UserResponse(
            user.getId(),
            user.getProfile().getFullName(),
            user.getUsername(),
            user.getRoles().stream().map(role -> role.getRoleName().name()).toList()
        );
    }
}
