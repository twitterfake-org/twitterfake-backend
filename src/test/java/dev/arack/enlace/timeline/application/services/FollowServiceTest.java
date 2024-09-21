package dev.arack.enlace.timeline.application.services;


import dev.arack.enlace.iam.domain.aggregate.UserEntity;
import dev.arack.enlace.iam.infrastructure.repository.UserRepository;
import dev.arack.enlace.timeline.application.ports.output.FollowPersistencePort;
import dev.arack.enlace.timeline.domain.model.FollowEntity;
import dev.arack.enlace.timeline.infrastructure.repository.FollowRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {
    @InjectMocks
    private FollowService followService;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private FollowPersistencePort followPersistencePort;
    @Mock
    private UserRepository UserRepository;

    @Test
    void followUser_Success() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;
        UserEntity follower = new UserEntity();
        UserEntity followed = new UserEntity();
        follower.setId(followerId);
        followed.setId(followedId);
        when(UserRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(UserRepository.findById(followedId)).thenReturn(Optional.of(followed));
        when(followRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(false);

        // Act
        followService.followUser(followerId, followedId);

        // Assert
        verify(followPersistencePort, times(1)).followUser(any(FollowEntity.class));
    }

    @Test
    void followUser_AlreadyFollowing() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;
        UserEntity follower = new UserEntity();
        UserEntity followed = new UserEntity();
        follower.setId(followerId);
        followed.setId(followedId);
        when(UserRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(UserRepository.findById(followedId)).thenReturn(Optional.of(followed));
        when(followRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(ValidationException.class, () -> {
            followService.followUser(followerId, followedId);
        });
        assertEquals("Already following this user", exception.getMessage());
        verify(followPersistencePort, never()).followUser(any(FollowEntity.class));
    }

    @Test
    void unfollowUser_Success() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;
        FollowEntity followEntity = new FollowEntity();
        when(followRepository.findByFollowerIdAndFollowedId(followerId, followedId)).thenReturn(Optional.of(followEntity));

        // Act
        followService.unfollowUser(followerId, followedId);

        // Assert
        verify(followPersistencePort, times(1)).unfollowUser(followEntity);
    }

    @Test
    void unfollowUser_NotFollowing() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;
        when(followRepository.findByFollowerIdAndFollowedId(followerId, followedId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.unfollowUser(followerId, followedId);
        });
        assertEquals("Follow relationship does not exist", exception.getMessage());
        verify(followPersistencePort, never()).unfollowUser(any(FollowEntity.class));
    }

    @Test
    void getFollowing_Success() {
        // Arrange
        Long userId = 1L;
        FollowEntity followEntity = new FollowEntity();
        when(followPersistencePort.getFollowing(userId)).thenReturn(List.of(followEntity));

        // Act
        List<FollowEntity> following = followService.getFollowing(userId);

        // Assert
        assertNotNull(following);
        assertEquals(1, following.size());
    }

    @Test
    void getFollowers_Success() {
        // Arrange
        Long userId = 1L;
        FollowEntity followEntity = new FollowEntity();
        when(followPersistencePort.getFollowers(userId)).thenReturn(List.of(followEntity));

        // Act
        List<FollowEntity> followers = followService.getFollowers(userId);

        // Assert
        assertNotNull(followers);
        assertEquals(1, followers.size());
    }
}
