package dev.arack.enlace.profile.application.port.output.persistence;

import dev.arack.enlace.profile.domain.entity.ProfileEntity;
import java.util.Optional;

public interface ProfilePersistence {
    ProfileEntity save(ProfileEntity profileEntity);
    Optional<ProfileEntity> findById(Long currentUserId);
    void deleteProfile(Long id);
    Optional<ProfileEntity> findByUsername(String username);
}
