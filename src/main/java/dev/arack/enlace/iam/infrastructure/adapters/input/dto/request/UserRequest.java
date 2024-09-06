package dev.arack.enlace.iam.infrastructure.adapters.input.dto.request;

import lombok.Getter;

@Getter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
