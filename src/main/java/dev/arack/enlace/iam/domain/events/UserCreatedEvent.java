package dev.arack.enlace.iam.domain.events;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final UserEntity user;

    public UserCreatedEvent(Object source, UserEntity user) {
        super(source);
        this.user = user;
    }
}
