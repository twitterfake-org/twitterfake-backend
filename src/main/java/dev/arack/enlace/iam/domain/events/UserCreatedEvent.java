package dev.arack.enlace.iam.domain.events;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final UserEntity user;
    private final String firstName;
    private final String lastName;

    public UserCreatedEvent(Object source, UserEntity user, String firstName, String lastName) {
        super(source);
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
