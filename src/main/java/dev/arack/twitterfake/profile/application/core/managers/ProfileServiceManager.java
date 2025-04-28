package dev.arack.twitterfake.profile.application.core.managers;

import dev.arack.twitterfake.iam.application.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import dev.arack.twitterfake.profile.application.dto.request.ProfileRequest;
import dev.arack.twitterfake.profile.application.dto.response.ProfileResponse;
import dev.arack.twitterfake.profile.application.port.input.services.ProfileService;
import dev.arack.twitterfake.profile.application.port.output.client.UserClient;
import dev.arack.twitterfake.profile.application.port.output.persistence.ProfilePersistence;
import dev.arack.twitterfake.profile.domain.aggregates.ProfileEntity;
import dev.arack.twitterfake.profile.domain.valueobject.Address;
import dev.arack.twitterfake.profile.domain.valueobject.FullName;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProfileServiceManager implements ProfileService {

    private final ProfilePersistence profilePersistence;
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

        profilePersistence.save(profileEntity);
    }

    @Override
    public ProfileResponse getProfile() {

        Long currentUserId = userClient.getCurrentUser().id();
        ProfileEntity profileEntity = profilePersistence.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return ProfileResponse.fromEntity(profileEntity);
    }

    @Override
    public ProfileResponse updateProfile(ProfileRequest profileRequest) {

        Long currentUserId = userClient.getCurrentUser().id();
        ProfileEntity profileEntity = profilePersistence.findById(currentUserId)
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

        profilePersistence.save(profileEntity);
        return ProfileResponse.fromEntity(profileEntity);
    }

    @Override
    public void deleteProfile() {

        Long currentUserId = userClient.getCurrentUser().id();
        profilePersistence.deleteById(currentUserId);
    }

    @Override
    public ProfileResponse getProfileByUsername(String username) {

        ProfileEntity profileEntity = profilePersistence.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return ProfileResponse.fromEntity(profileEntity);
    }
}
