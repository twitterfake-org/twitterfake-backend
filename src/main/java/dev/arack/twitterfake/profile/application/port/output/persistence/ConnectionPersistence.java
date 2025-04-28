package dev.arack.twitterfake.profile.application.port.output.persistence;

import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import dev.arack.twitterfake.profile.domain.entities.ConnectionEntity;

import java.util.List;
import java.util.Optional;

public interface ConnectionPersistence {
    void followUser(ConnectionEntity follow);
    void unfollowUser(ConnectionEntity follow);
    List<ConnectionEntity> getFollowing(Long userId);
    List<ConnectionEntity> getFollowers(Long userId);
    Optional<ConnectionEntity> findByFollowerIdAndFollowedId(Long followerId, Long followedId);
    boolean existsByFollowerAndFollowed(UserEntity follower, UserEntity followed);
}
