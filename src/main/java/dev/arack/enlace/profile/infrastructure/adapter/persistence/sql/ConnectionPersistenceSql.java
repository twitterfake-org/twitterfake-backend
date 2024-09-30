package dev.arack.enlace.profile.infrastructure.adapter.persistence.sql;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.profile.application.port.output.persistence.ConnectionPersistence;
import dev.arack.enlace.profile.domain.entity.ConnectionEntity;
import dev.arack.enlace.profile.infrastructure.repository.ConnectionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConnectionPersistenceSql implements ConnectionPersistence {

    private final ConnectionJpaRepository connectionJpaRepository;

    @Override
    public void followUser(ConnectionEntity follow) { connectionJpaRepository.save(follow); }

    @Override
    public void unfollowUser(ConnectionEntity follow) {
        connectionJpaRepository.delete(follow);
    }

    @Override
    public List<ConnectionEntity> getFollowing(Long userId) {
        return connectionJpaRepository.findByFollowingByUserId(userId);
    }

    @Override
    public List<ConnectionEntity> getFollowers(Long userId) {
        return connectionJpaRepository.findByFollowersByUserId(userId);
    }

    @Override
    public Optional<ConnectionEntity> findByFollowerIdAndFollowedId(Long followerId, Long followedId) {
        return connectionJpaRepository.findByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public boolean existsByFollowerAndFollowed(UserEntity follower, UserEntity followed) {
        return connectionJpaRepository.existsByFollowerAndFollowed(follower, followed);
    }
}
