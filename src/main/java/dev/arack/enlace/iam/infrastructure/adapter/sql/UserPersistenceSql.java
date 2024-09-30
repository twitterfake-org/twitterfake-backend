package dev.arack.enlace.iam.infrastructure.adapter.sql;

import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceSql implements UserPersistence {

    private final UserJpaRepository userJpaRepository;

    @Override
    public List<UserEntity> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userJpaRepository.save(userEntity);
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        userJpaRepository.delete(userEntity);
    }

    @Override
    public Optional<UserEntity> findUserEntityByUsername(String username) {
        return userJpaRepository.findUserEntityByUsername(username);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userJpaRepository.save(userEntity);
    }
}
