package dev.arack.enlace.profile.infrastructure.repository;

import dev.arack.enlace.profile.domain.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileJpaRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByUser_Id(Long userId);
    Optional<ProfileEntity> findByUser_Username(String username);
}
