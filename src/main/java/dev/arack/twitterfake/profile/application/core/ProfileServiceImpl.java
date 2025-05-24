package dev.arack.twitterfake.profile.application.core;

import dev.arack.twitterfake.iam.infrastructure.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import dev.arack.twitterfake.profile.infrastructure.dto.request.ProfileRequest;
import dev.arack.twitterfake.profile.infrastructure.dto.response.ProfileResponse;
import dev.arack.twitterfake.profile.domain.services.ProfileService;
import dev.arack.twitterfake.profile.infrastructure.client.UserClient;
import dev.arack.twitterfake.profile.domain.model.aggregates.ProfileEntity;
import dev.arack.twitterfake.profile.domain.model.valueobject.Address;
import dev.arack.twitterfake.profile.domain.model.valueobject.FullName;
import dev.arack.twitterfake.profile.infrastructure.repository.ProfileRepository;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserClient userClient;

    @Override
    public void createProfile(UserEntity userEntity, SocialRequest social) {

        ProfileEntity profileEntity = ProfileEntity.builder()
                .fullName(new FullName(
                        social.getFirstName(),
                        social.getLastName()
                ))
                .email(social.getEmail())
                .address(new Address("", "", "", "", ""))
                .photoUrl(social.getPhotoUrl())
                .user(userEntity)
                .build();

        profileRepository.save(profileEntity);
    }

    @Override
    public ProfileResponse getProfile() {

        Long currentUserId = userClient.getCurrentUser().id();
        ProfileEntity profileEntity = profileRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return ProfileResponse.fromEntity(profileEntity);
    }

    @Override
    public ProfileResponse updateProfile(ProfileRequest profileRequest) {

        Long currentUserId = userClient.getCurrentUser().id();
        ProfileEntity profileEntity = profileRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        profileEntity.setFullName(new FullName(
                profileRequest.firstName(),
                profileRequest.lastName()
                ));
        profileEntity.setEmail(profileRequest.email());
        profileEntity.setAddress(new Address(
                profileRequest.street(),
                profileRequest.number(),
                profileRequest.city(),
                profileRequest.zipCode(),
                profileRequest.country()
                ));

        profileRepository.save(profileEntity);
        return ProfileResponse.fromEntity(profileEntity);
    }

    @Override
    public void deleteProfile() {

        Long currentUserId = userClient.getCurrentUser().id();
        profileRepository.deleteById(currentUserId);
    }

    @Override
    public ProfileResponse getProfileByUsername(String username) {

        ProfileEntity profileEntity = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return ProfileResponse.fromEntity(profileEntity);
    }
}
