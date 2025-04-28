package dev.arack.twitterfake.iam.application.port.output.persistence;

import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserPersistence {
    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long userId);
    void deleteUser(UserEntity userEntity);
    Optional<UserEntity> findUserEntityByUsername(String username);
    UserEntity save(UserEntity userEntity);
    boolean existsByUsername(String username);
}
