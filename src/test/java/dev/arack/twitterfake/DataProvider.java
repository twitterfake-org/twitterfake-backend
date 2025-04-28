package dev.arack.twitterfake;

import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public class DataProvider {

    public static UserDetails userDetailsMock() {
        return new User(
                "username",
                "encodedPassword",
                true,
                true,
                true,
                true,
                Set.of()
        );
    }

    public static UserEntity userEntityMock() {
        return UserEntity.builder()
                .id(1L)
                .username("username")
                .password("encodedPassword")
                .build();
    }
}