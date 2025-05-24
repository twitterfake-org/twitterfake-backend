package dev.arack.twitterfake.profile.application.core;

import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import dev.arack.twitterfake.iam.infrastructure.repository.UserRepository;
import dev.arack.twitterfake.profile.domain.services.ConnectionService;
import dev.arack.twitterfake.profile.infrastructure.client.UserClient;
import dev.arack.twitterfake.profile.infrastructure.repository.ConnectionRepository;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import dev.arack.twitterfake.profile.infrastructure.dto.response.FollowResponse;
import dev.arack.twitterfake.profile.domain.model.entities.ConnectionEntity;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final UserClient userClient;

    @Override
    public void followUser(Long followedId) {

        Long currentUserId = userClient.getCurrentUser().id();

        if (currentUserId.equals(followedId)) {
            throw new ValidationException("Cannot follow yourself");
        }
        UserEntity follower = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));

        UserEntity followed = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Followed user not found"));

        if (connectionRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new ValidationException("Already following this user");
        }
        ConnectionEntity connectionEntity = new ConnectionEntity();
        connectionEntity.setFollower(follower);
        connectionEntity.setFollowed(followed);

        connectionRepository.save(connectionEntity);
    }

    @Override
    public void unfollowUser(Long followedId) {

        Long currentUserId = userClient.getCurrentUser().id();

        if (currentUserId.equals(followedId)) {
            throw new IllegalArgumentException("Cannot unfollow yourself");
        }
        ConnectionEntity connectionEntity = connectionRepository.findByFollowerIdAndFollowedId(currentUserId, followedId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship does not exist"));

        connectionRepository.delete(connectionEntity);
    }

    @Override
    public List<FollowResponse> getFollowing(Long userId) {

        List<ConnectionEntity> connectionEntityList = connectionRepository.findByFollowingByUserId(userId);
        return FollowResponse.fromFollowEntityList(connectionEntityList);
    }

    @Override
    public List<FollowResponse> getFollowers(Long userId) {

        List<ConnectionEntity> connectionEntityList = connectionRepository.findByFollowersByUserId(userId);
        return FollowResponse.fromFollowEntityList(connectionEntityList);
    }
}
