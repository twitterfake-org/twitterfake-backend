package dev.arack.enlace.post.application.dto.response;

import dev.arack.enlace.profile.domain.entities.ConnectionEntity;

import java.util.List;

public record FollowResponse(
        Long id,
        String fullName,
        String username
) {
    public static List<FollowResponse> fromFollowEntityList(List<ConnectionEntity> connectionEntityList) {
        return connectionEntityList.stream()
                .map(follow -> new FollowResponse(
                        follow.getId(),
                        follow.getFollowed().getProfile().getFullName(),
                        follow.getFollowed().getUsername()
                ))
                .toList();
    }
}