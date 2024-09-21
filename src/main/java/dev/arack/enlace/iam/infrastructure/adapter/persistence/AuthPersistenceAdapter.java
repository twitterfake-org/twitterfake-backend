package dev.arack.enlace.iam.infrastructure.adapter.persistence;

import dev.arack.enlace.iam.application.port.output.persistence.AuthPersistencePort;
import dev.arack.enlace.iam.domain.aggregate.UserEntity;
import dev.arack.enlace.iam.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthPersistencePort {

    private final UserRepository userRepository;

    @Override
    public void register(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
