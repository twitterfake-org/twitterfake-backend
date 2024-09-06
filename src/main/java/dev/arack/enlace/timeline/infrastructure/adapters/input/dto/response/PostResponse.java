package dev.arack.enlace.timeline.infrastructure.adapters.input.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResponse {
    private Long id;
    private String content;
    private String username;
    private String createdAt;
}
