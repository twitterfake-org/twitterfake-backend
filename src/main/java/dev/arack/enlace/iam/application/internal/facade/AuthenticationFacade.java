package dev.arack.enlace.iam.application.internal.facade;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;

public interface AuthenticationFacade {
    UserEntity getCurrentUser();
}