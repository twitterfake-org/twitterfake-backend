package dev.arack.enlace.timeline.application.ports.input;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.List;

public interface FollowServiceInput {
    void followUser(UserEntity follower, UserEntity followed);
    void unfollowUser(Long followerId, Long followedId);
    List<UserResponse> getFollowing(UserEntity user);
    List<UserResponse> getFollowers(UserEntity user);
}
