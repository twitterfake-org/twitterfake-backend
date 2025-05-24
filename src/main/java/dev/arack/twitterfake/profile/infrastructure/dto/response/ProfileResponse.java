package dev.arack.twitterfake.profile.infrastructure.dto.response;

import dev.arack.twitterfake.profile.domain.model.aggregates.ProfileEntity;

public record ProfileResponse(
        Long id,
        String fullName,
        String username,
        String email,
        String address,
        String photoUrl
) {
    public static ProfileResponse fromEntity(ProfileEntity profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getFullName(),
                profile.getUser().getUsername(),
                profile.getEmail(),
                profile.getAddress(),
                profile.getPhotoUrl()
        );
    }
}
