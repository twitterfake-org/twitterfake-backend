package dev.arack.twitterfake.profile.application.core.managers;

import dev.arack.twitterfake.iam.application.port.output.persistence.UserPersistence;
import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import dev.arack.twitterfake.profile.application.port.input.services.ConnectionService;
import dev.arack.twitterfake.profile.application.port.output.client.UserClient;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import dev.arack.twitterfake.profile.application.dto.response.FollowResponse;
import dev.arack.twitterfake.profile.application.port.output.persistence.ConnectionPersistence;
import dev.arack.twitterfake.profile.domain.entities.ConnectionEntity;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceManager implements ConnectionService {

    private final UserPersistence userPersistence;
    private final ConnectionPersistence connectionPersistence;
    private final UserClient userClient;

    @Override
    public void followUser(Long followedId) {

        Long currentUserId = userClient.getCurrentUser().id();

        if (currentUserId.equals(followedId)) {
            throw new ValidationException("Cannot follow yourself");
        }
        UserEntity follower = userPersistence.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));

        UserEntity followed = userPersistence.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Followed user not found"));

        if (connectionPersistence.existsByFollowerAndFollowed(follower, followed)) {
            throw new ValidationException("Already following this user");
        }
        ConnectionEntity connectionEntity = new ConnectionEntity();
        connectionEntity.setFollower(follower);
        connectionEntity.setFollowed(followed);

        connectionPersistence.followUser(connectionEntity);
    }

    @Override
    public void unfollowUser(Long followedId) {

        Long currentUserId = userClient.getCurrentUser().id();

        if (currentUserId.equals(followedId)) {
            throw new IllegalArgumentException("Cannot unfollow yourself");
        }
        ConnectionEntity connectionEntity = connectionPersistence.findByFollowerIdAndFollowedId(currentUserId, followedId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship does not exist"));

        connectionPersistence.unfollowUser(connectionEntity);
    }

    @Override
    public List<FollowResponse> getFollowing(Long userId) {

        List<ConnectionEntity> connectionEntityList = connectionPersistence.getFollowing(userId);
        return FollowResponse.fromFollowEntityList(connectionEntityList);
    }

    @Override
    public List<FollowResponse> getFollowers(Long userId) {

        List<ConnectionEntity> connectionEntityList = connectionPersistence.getFollowers(userId);
        return FollowResponse.fromFollowEntityList(connectionEntityList);
    }
}
