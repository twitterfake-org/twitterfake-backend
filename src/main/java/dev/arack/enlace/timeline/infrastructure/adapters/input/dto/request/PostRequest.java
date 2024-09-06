package dev.arack.enlace.timeline.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostRequest {

    @NotBlank(message = "Content cannot be blank")
    private String content;
}
