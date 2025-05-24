package dev.arack.twitterfake.profile.domain.services;

import dev.arack.twitterfake.iam.infrastructure.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import dev.arack.twitterfake.profile.infrastructure.dto.request.ProfileRequest;
import dev.arack.twitterfake.profile.infrastructure.dto.response.ProfileResponse;

public interface ProfileService {
    void createProfile(UserEntity user, SocialRequest social);
    ProfileResponse getProfile();
    ProfileResponse updateProfile(ProfileRequest profileRequest);
    void deleteProfile();
    ProfileResponse getProfileByUsername(String username);
}