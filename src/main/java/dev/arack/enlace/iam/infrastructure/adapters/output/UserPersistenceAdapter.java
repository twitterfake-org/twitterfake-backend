package dev.arack.enlace.iam.infrastructure.adapters.output;

import dev.arack.enlace.iam.application.ports.output.UserPersistencePort;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }
}
