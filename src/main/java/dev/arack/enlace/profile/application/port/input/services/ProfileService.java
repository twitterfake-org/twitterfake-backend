package dev.arack.enlace.profile.application.port.input.services;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.profile.application.dto.request.ProfileRequest;
import dev.arack.enlace.profile.application.dto.response.ProfileResponse;

public interface ProfileService {
    void createProfile(UserEntity request);
    ProfileResponse getProfile();
    ProfileResponse updateProfile(ProfileRequest profileRequest);
    void deleteProfile();
    ProfileResponse getProfileByUsername(String username);
}
