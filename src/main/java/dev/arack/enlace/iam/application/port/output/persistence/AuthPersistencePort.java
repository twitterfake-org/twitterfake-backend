package dev.arack.enlace.iam.application.port.output.persistence;

import dev.arack.enlace.iam.domain.aggregate.UserEntity;

public interface AuthPersistencePort {
    void register(UserEntity userEntity);
}
