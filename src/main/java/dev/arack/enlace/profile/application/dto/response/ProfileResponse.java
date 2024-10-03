package dev.arack.enlace.profile.application.dto.response;

import dev.arack.enlace.profile.domain.aggregates.ProfileEntity;

public record ProfileResponse(
        Long id,
        String fullName,
        String email,
        String address
) {
    public static ProfileResponse fromEntity(ProfileEntity profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getFullName(),
                profile.getEmail(),
                profile.getAddress()
        );
    }
}
