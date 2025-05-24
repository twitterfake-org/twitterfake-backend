package dev.arack.twitterfake.iam.domain.model.events;

import dev.arack.twitterfake.iam.infrastructure.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final UserEntity user;
    private final SocialRequest socialRequest;

    public UserCreatedEvent(Object source, UserEntity user, SocialRequest socialRequest) {
        super(source);
        this.user = user;
        this.socialRequest = socialRequest;
    }
}
