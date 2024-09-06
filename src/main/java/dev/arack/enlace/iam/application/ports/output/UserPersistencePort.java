package dev.arack.enlace.iam.application.ports.output;

import dev.arack.enlace.iam.domain.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    List<UserEntity> findAll();
    Optional<UserEntity> findByUsername(String username);
    void updateUser(UserEntity userEntity);
    void deleteUser(UserEntity userEntity);
}
