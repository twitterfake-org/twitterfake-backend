package dev.arack.enlace.iam.infrastructure.adapters.input.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
}
