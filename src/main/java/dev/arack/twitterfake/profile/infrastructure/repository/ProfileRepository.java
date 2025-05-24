package dev.arack.twitterfake.profile.infrastructure.repository;

import dev.arack.twitterfake.profile.domain.model.aggregates.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByUser_Id(Long userId);
    Optional<ProfileEntity> findByUser_Username(String username);
}
