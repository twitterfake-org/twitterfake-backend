package dev.arack.twitterfake.profile.application.dto.response;

import dev.arack.twitterfake.profile.domain.aggregates.ProfileEntity;

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
