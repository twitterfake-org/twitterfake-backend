package dev.arack.enlace.timeline.infrastructure.adapters.output;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.timeline.application.ports.output.FollowPersistenceOutput;
import dev.arack.enlace.timeline.domain.model.FollowEntity;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements FollowPersistenceOutput {
    private final FollowRepository followRepository;

    @Override
    public void followUser(FollowEntity follow) {
        followRepository.save(follow);
    }

    @Override
    public void unfollowUser(FollowEntity follow) {
        followRepository.delete(follow);
    }

    @Override
    public List<FollowEntity> getFollowing(Long userId) {
        return followRepository.findByFollowingByUserId(userId);
    }

    @Override
    public List<FollowEntity> getFollowers(Long userId) {
        return followRepository.findByFollowersByUserId(userId);
    }
}
