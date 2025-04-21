package dev.arack.enlace.profile.application.core.handler;

import dev.arack.enlace.iam.domain.events.UserCreatedEvent;
import dev.arack.enlace.profile.application.port.input.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final ProfileService profileService;

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        profileService.createProfile(event.getUser(), event.getFirstName(), event.getLastName());
    }
}

