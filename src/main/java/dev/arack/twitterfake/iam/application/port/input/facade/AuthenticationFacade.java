package dev.arack.twitterfake.iam.application.port.input.facade;

import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;

public interface AuthenticationFacade {
    UserEntity getCurrentUser();
}