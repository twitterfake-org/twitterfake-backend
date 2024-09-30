package dev.arack.enlace.profile.infrastructure.adapter.sql;

import dev.arack.enlace.profile.application.port.output.persistence.ProfilePersistence;
import dev.arack.enlace.profile.domain.entity.ProfileEntity;
import dev.arack.enlace.profile.infrastructure.repository.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfilePersistenceSql implements ProfilePersistence {

    private final ProfileJpaRepository profileJpaRepository;

    @Override
    public ProfileEntity save(ProfileEntity profileEntity) {
        return profileJpaRepository.save(profileEntity);
    }

    @Override
    public Optional<ProfileEntity> findById(Long currentUserId) {
        return profileJpaRepository.findByUser_Id(currentUserId);
    }

    @Override
    public void deleteProfile(Long id) {
        profileJpaRepository.deleteById(id);
    }

    @Override
    public Optional<ProfileEntity> findByUsername(String username) {
        return profileJpaRepository.findByUser_Username(username);
    }
}
