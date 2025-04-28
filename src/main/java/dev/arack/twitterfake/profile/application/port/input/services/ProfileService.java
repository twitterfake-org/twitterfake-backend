package dev.arack.twitterfake.profile.application.port.input.services;

import dev.arack.twitterfake.iam.application.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import dev.arack.twitterfake.profile.application.dto.request.ProfileRequest;
import dev.arack.twitterfake.profile.application.dto.response.ProfileResponse;

public interface ProfileService {
    void createProfile(UserEntity user, SocialRequest social);
    ProfileResponse getProfile();
    ProfileResponse updateProfile(ProfileRequest profileRequest);
    void deleteProfile();
    ProfileResponse getProfileByUsername(String username);
}