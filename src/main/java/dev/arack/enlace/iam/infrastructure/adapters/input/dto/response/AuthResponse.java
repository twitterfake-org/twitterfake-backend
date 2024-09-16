package dev.arack.enlace.iam.infrastructure.adapters.input.dto.response;

public record AuthResponse(
        String username,
        String message,
        Boolean status,
        String jwt) {
}
