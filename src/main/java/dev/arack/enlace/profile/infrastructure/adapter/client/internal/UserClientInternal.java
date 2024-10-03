package dev.arack.enlace.profile.infrastructure.adapter.client.internal;

import dev.arack.enlace.iam.application.dto.response.UserResponse;
import dev.arack.enlace.iam.application.port.input.facade.AuthenticationFacade;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.profile.application.port.output.client.UserClient;
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
