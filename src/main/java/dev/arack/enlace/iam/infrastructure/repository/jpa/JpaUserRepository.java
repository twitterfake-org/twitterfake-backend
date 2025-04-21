package dev.arack.enlace.iam.infrastructure.repository.jpa;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);
    boolean existsByUsername(String username);
}
