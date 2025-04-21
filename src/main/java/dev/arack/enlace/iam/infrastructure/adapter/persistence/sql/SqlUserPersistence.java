package dev.arack.enlace.iam.infrastructure.adapter.persistence.sql;

import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.infrastructure.repository.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SqlUserPersistence implements UserPersistence {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public List<UserEntity> findAll() {
        return jpaUserRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return jpaUserRepository.findById(userId);
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        jpaUserRepository.delete(userEntity);
    }

    @Override
    public Optional<UserEntity> findUserEntityByUsername(String username) {
        return jpaUserRepository.findUserEntityByUsername(username);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return jpaUserRepository.save(userEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }
}
