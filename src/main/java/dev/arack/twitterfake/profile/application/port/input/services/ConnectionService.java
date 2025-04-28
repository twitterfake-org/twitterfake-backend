package dev.arack.twitterfake.profile.application.port.input.services;

import dev.arack.twitterfake.profile.application.dto.response.FollowResponse;
import dev.arack.twitterfake.profile.domain.entities.ConnectionEntity;

import java.util.List;

/**
 * Interface for follow management services, providing methods to handle user following and unfollowing actions.
 */
public interface ConnectionService {
    /**
     * Allows a user to follow another user.
     *
     * @param followedId The ID of the user to be followed.
     */
    void followUser(Long followedId);

    /**
     * Allows a user to unfollow another user.
     *
     * @param followedId The ID of the user to be unfollowed.
     */
    void unfollowUser(Long followedId);

    /**
     * Retrieves a list of users that a specified user is following.
     *
     * @return A {@link List} of {@link ConnectionEntity} objects representing the users being followed by the specified user.
     */
    List<FollowResponse> getFollowing(Long userId);

    /**
     * Retrieves a list of users who are following a specified user.
     *
     * @return A {@link List} of {@link ConnectionEntity} objects representing the users following the specified user.
     */
    List<FollowResponse> getFollowers(Long userId);
}
