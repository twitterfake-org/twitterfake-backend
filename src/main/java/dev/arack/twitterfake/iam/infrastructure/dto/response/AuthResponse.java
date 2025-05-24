package dev.arack.twitterfake.iam.infrastructure.dto.response;

public record AuthResponse(
        String username,
        String message,
        Boolean status,
        String token) {

    public static AuthResponse createInvalidAuthResponse() {
        return new AuthResponse(
                null,
                "Invalid ID token",
                false,
                null
        );
    }
}
