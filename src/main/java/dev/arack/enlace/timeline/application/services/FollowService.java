package dev.arack.enlace.timeline.application.services;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.IUserRepository;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import dev.arack.enlace.timeline.application.ports.output.FollowPersistencePort;
import dev.arack.enlace.timeline.domain.model.FollowEntity;
import dev.arack.enlace.timeline.application.ports.input.FollowServicePort;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.FollowRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowServicePort {

    private final FollowRepository followRepository;
    private final IUserRepository IUserRepository;
    private final FollowPersistencePort followPersistencePort;

    @Override
    public void followUser(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new ValidationException("Cannot follow yourself");
        }
        UserEntity follower = IUserRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        UserEntity followed = IUserRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Followed user not found"));

        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new ValidationException("Already following this user");
        }
        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(follower);
        followEntity.setFollowed(followed);

        followPersistencePort.followUser(followEntity);
    }

    @Override
    public void unfollowUser(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Cannot unfollow yourself");
        }
        FollowEntity followEntity = followRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship does not exist"));

        followPersistencePort.unfollowUser(followEntity);
    }

    @Override
    public List<FollowEntity> getFollowing(Long userId) {
        return followPersistencePort.getFollowing(userId);
    }

    @Override
    public List<FollowEntity> getFollowers(Long userId) {
        return followPersistencePort.getFollowers(userId);
    }
}
