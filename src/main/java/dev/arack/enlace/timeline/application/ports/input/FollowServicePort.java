package dev.arack.enlace.timeline.application.ports.input;

import dev.arack.enlace.timeline.domain.model.FollowEntity;

import java.util.List;

/**
 * Interface for follow management services, providing methods to handle user following and unfollowing actions.
 */
public interface FollowServicePort {
    /**
     * Allows a user to follow another user.
     *
     * @param followerId The ID of the user who wants to follow.
     * @param followedId The ID of the user to be followed.
     */
    void followUser(Long followerId, Long followedId);

    /**
     * Allows a user to unfollow another user.
     *
     * @param followerId The ID of the user who wants to unfollow.
     * @param followedId The ID of the user to be unfollowed.
     */
    void unfollowUser(Long followerId, Long followedId);

    /**
     * Retrieves a list of users that a specified user is following.
     *
     * @param userId The ID of the user whose following list is to be retrieved.
     * @return A {@link List} of {@link FollowEntity} objects representing the users being followed by the specified user.
     */
    List<FollowEntity> getFollowing(Long userId);

    /**
     * Retrieves a list of users who are following a specified user.
     *
     * @param userId The ID of the user whose followers list is to be retrieved.
     * @return A {@link List} of {@link FollowEntity} objects representing the users following the specified user.
     */
    List<FollowEntity> getFollowers(Long userId);
}
