package dev.arack.enlace.iam.application.port.persistence;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserPersistence {
    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long userId);
    void updateUser(UserEntity userEntity);
    void deleteUser(UserEntity userEntity);
    Optional<UserEntity> findUserEntityByUsername(String username);
    UserEntity save(UserEntity userEntity);
}
