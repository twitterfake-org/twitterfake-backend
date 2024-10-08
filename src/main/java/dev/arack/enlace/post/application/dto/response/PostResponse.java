package dev.arack.enlace.post.application.dto.response;

import dev.arack.enlace.post.domain.aggregates.PostEntity;
import java.util.List;

public record PostResponse (
    Long id,
    String content,
    String username,
    String createdAt
) {
    public static PostResponse fromPostEntity(PostEntity postEntity) {
        return new PostResponse(
                postEntity.getId(),
                postEntity.getContent(),
                postEntity.getUserEntity().getUsername(),
                postEntity.getFormattedCreatedAt()
        );
    }

    public static List<PostResponse> fromPostEntityList(List<PostEntity> postEntityList) {
        return postEntityList.stream()
                .map(PostResponse::fromPostEntity)
                .toList();
    }
}
