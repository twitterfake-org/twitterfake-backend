package dev.arack.enlace.iam.application.facades;

import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFacade implements AuthenticationFacade {

    private final UserPersistence userPersistence;

    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            String username = (String) authentication.getPrincipal();
            return userPersistence.findUserEntityByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
