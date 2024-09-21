package dev.arack.enlace.iam.application.port.output.persistence;

import dev.arack.enlace.iam.domain.aggregate.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long userId);
    void updateUser(UserEntity userEntity);
    void deleteUser(UserEntity userEntity);
    Optional<UserEntity> findUserEntityByUsername(String username);
    UserEntity save(UserEntity userEntity);
}
