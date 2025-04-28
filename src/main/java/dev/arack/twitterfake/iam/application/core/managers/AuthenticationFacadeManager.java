package dev.arack.twitterfake.iam.application.core.managers;

import dev.arack.twitterfake.iam.application.port.input.facade.AuthenticationFacade;
import dev.arack.twitterfake.iam.application.port.output.persistence.UserPersistence;
import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacadeManager implements AuthenticationFacade {

    private final UserPersistence userPersistence;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userPersistence.findUserEntityByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }
    }
}
