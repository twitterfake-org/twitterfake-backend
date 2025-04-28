package dev.arack.twitterfake.profile.infrastructure.adapter.persistence.sql;

import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import dev.arack.twitterfake.profile.application.port.output.persistence.ConnectionPersistence;
import dev.arack.twitterfake.profile.domain.entities.ConnectionEntity;
import dev.arack.twitterfake.profile.infrastructure.repository.jpa.JpaConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SqlConnectionPersistence implements ConnectionPersistence {

    private final JpaConnectionRepository jpaConnectionRepository;

    @Override
    public void followUser(ConnectionEntity follow) { jpaConnectionRepository.save(follow); }

    @Override
    public void unfollowUser(ConnectionEntity follow) {
        jpaConnectionRepository.delete(follow);
    }

    @Override
    public List<ConnectionEntity> getFollowing(Long userId) {
        return jpaConnectionRepository.findByFollowingByUserId(userId);
    }

    @Override
    public List<ConnectionEntity> getFollowers(Long userId) {
        return jpaConnectionRepository.findByFollowersByUserId(userId);
    }

    @Override
    public Optional<ConnectionEntity> findByFollowerIdAndFollowedId(Long followerId, Long followedId) {
        return jpaConnectionRepository.findByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public boolean existsByFollowerAndFollowed(UserEntity follower, UserEntity followed) {
        return jpaConnectionRepository.existsByFollowerAndFollowed(follower, followed);
    }
}
