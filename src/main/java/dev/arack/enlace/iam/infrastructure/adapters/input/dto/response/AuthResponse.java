package dev.arack.enlace.iam.infrastructure.adapters.input.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private String username;
    private String token;
}
