package dev.arack.enlace.timeline.application.ports.output;

import dev.arack.enlace.timeline.domain.model.FollowEntity;

import java.util.List;

public interface FollowPersistenceOutput {
    void followUser(FollowEntity follow);
    void unfollowUser(FollowEntity follow);
    List<FollowEntity> getFollowing(Long userId);
    List<FollowEntity> getFollowers(Long userId);
}
