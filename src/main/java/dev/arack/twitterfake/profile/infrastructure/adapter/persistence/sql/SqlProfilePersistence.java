package dev.arack.twitterfake.profile.infrastructure.adapter.persistence.sql;

import dev.arack.twitterfake.profile.application.port.output.persistence.ProfilePersistence;
import dev.arack.twitterfake.profile.domain.aggregates.ProfileEntity;
import dev.arack.twitterfake.profile.infrastructure.repository.jpa.JpaProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SqlProfilePersistence implements ProfilePersistence {

    private final JpaProfileRepository jpaProfileRepository;

    @Override
    public ProfileEntity save(ProfileEntity profileEntity) {
        return jpaProfileRepository.save(profileEntity);
    }

    @Override
    public Optional<ProfileEntity> findById(Long currentUserId) {
        return jpaProfileRepository.findByUser_Id(currentUserId);
    }

    @Override
    public void deleteById(Long id) {
        jpaProfileRepository.deleteById(id);
    }

    @Override
    public Optional<ProfileEntity> findByUsername(String username) {
        return jpaProfileRepository.findByUser_Username(username);
    }
}
