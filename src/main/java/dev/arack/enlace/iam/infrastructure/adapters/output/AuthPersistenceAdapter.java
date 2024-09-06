package dev.arack.enlace.iam.infrastructure.adapters.output;

import dev.arack.enlace.iam.application.ports.output.AuthPersistenceOutput;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthPersistenceOutput {

    private final UserRepository userRepository;

    @Override
    public void register(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
