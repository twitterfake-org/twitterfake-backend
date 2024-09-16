package dev.arack.enlace.iam.application.ports.output;

import dev.arack.enlace.iam.domain.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserPersistencePort {
    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long userId);
    void updateUser(UserEntity userEntity);
    void deleteUser(UserEntity userEntity);
    Optional<UserEntity> findUserEntityByUsername(String username);
    UserEntity save(UserEntity userEntity);
}
