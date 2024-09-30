package dev.arack.enlace.timeline.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostRequest (
    @NotBlank(message = "Content cannot be blank") String content
) {
}
