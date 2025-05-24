package dev.arack.twitterfake.profile.infrastructure.client;

import dev.arack.twitterfake.iam.infrastructure.dto.response.UserResponse;
import dev.arack.twitterfake.iam.application.facade.AuthenticationFacade;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientInternal implements UserClient {

    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserResponse getCurrentUser() {
        UserEntity currentUser = authenticationFacade.getCurrentUser();
        if (currentUser == null) {
            // Maneja el caso en el que no haya un usuario autenticado
            return null; // O puedes devolver una respuesta predeterminada
        }
        return UserResponse.fromEntity(currentUser);
    }
}
