package dev.arack.enlace.iam.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String username,
        @NotBlank String password) {
}