package dev.arack.enlace.timeline.application.services;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import dev.arack.enlace.timeline.application.ports.output.FollowPersistenceOutput;
import dev.arack.enlace.timeline.domain.model.FollowEntity;
import dev.arack.enlace.timeline.application.ports.input.FollowServiceInput;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowServiceInput {

    private final FollowRepository followRepository;
    private final FollowPersistenceOutput followPersistenceOutput;
    private final ModelMapper modelMapper;

    @Override
    public void followUser(UserEntity follower, UserEntity followed) {
        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new IllegalArgumentException("Already following this user");
        }
        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(follower);
        followEntity.setFollowed(followed);
        followPersistenceOutput.followUser(followEntity);
    }

    @Override
    public void unfollowUser(Long followerId, Long followedId) {
        FollowEntity followEntity = followRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship does not exist"));
        followPersistenceOutput.unfollowUser(followEntity);
    }

    @Override
    public List<UserResponse> getFollowing(UserEntity user) {
        List<FollowEntity> followers = followPersistenceOutput.getFollowing(user.getId());
        return followers.stream()
                .map(follow -> modelMapper.map(follow.getFollowed(), UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getFollowers(UserEntity user) {
        List<FollowEntity> followers = followPersistenceOutput.getFollowers(user.getId());
        return followers.stream()
                .map(follow -> modelMapper.map(follow.getFollower(), UserResponse.class))
                .collect(Collectors.toList());
    }
}
