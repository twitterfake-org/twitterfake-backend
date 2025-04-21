package dev.arack.enlace.iam.application.dto.response;

public record AuthResponse(
        String username,
        String message,
        Boolean status,
        String token) {
}
