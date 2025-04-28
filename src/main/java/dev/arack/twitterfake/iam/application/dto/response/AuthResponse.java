package dev.arack.twitterfake.iam.application.dto.response;

public record AuthResponse(
        String username,
        String message,
        Boolean status,
        String token) {
}
