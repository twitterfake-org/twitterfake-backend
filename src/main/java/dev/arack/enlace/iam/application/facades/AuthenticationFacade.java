package dev.arack.enlace.iam.application.facades;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;

public interface AuthenticationFacade {
    UserEntity getCurrentUser();
}