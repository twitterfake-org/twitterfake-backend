package dev.arack.enlace.post.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostRequest (
    @NotBlank(message = "Content cannot be blank") String content
) {
}
