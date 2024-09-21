package dev.arack.enlace.profile.application.port.services;

import dev.arack.enlace.profile.application.dto.ProfileRequest;
import dev.arack.enlace.profile.application.dto.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest profileRequest);
    ProfileResponse getProfile();
    ProfileResponse updateProfile(ProfileRequest profileRequest);
    void deleteProfile();
}
