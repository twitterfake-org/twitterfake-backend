package dev.arack.enlace.iam.application.port.input.facade;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;

public interface AuthenticationFacade {
    UserEntity getCurrentUser();
}