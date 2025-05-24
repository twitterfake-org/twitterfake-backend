package dev.arack.twitterfake.profile.application.handler;

import dev.arack.twitterfake.iam.domain.model.events.UserCreatedEvent;
import dev.arack.twitterfake.profile.domain.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final ProfileService profileService;

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        profileService.createProfile(event.getUser(), event.getSocialRequest());
    }
}

