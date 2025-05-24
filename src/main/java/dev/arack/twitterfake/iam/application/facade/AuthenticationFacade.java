package dev.arack.twitterfake.iam.application.facade;

import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;

public interface AuthenticationFacade {
    UserEntity getCurrentUser();
}