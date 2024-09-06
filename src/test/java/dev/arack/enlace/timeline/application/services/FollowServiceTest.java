package dev.arack.enlace.timeline.application.services;


import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import dev.arack.enlace.timeline.application.ports.output.FollowPersistenceOutput;
import dev.arack.enlace.timeline.domain.model.FollowEntity;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.FollowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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
    private FollowPersistenceOutput followPersistenceOutput;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void followUser_Success() {
        // Arrange
        UserEntity follower = new UserEntity();
        UserEntity followed = new UserEntity();
        when(followRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(false);

        // Act
        followService.followUser(follower, followed);

        // Assert
        verify(followPersistenceOutput, times(1)).followUser(any(FollowEntity.class));
    }
    @Test
    void followUser_AlreadyFollowing() {
        // Arrange
        UserEntity follower = new UserEntity();
        UserEntity followed = new UserEntity();
        when(followRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.followUser(follower, followed);
        });
        assertEquals("Already following this user", exception.getMessage());
        verify(followPersistenceOutput, never()).followUser(any(FollowEntity.class));
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
        verify(followPersistenceOutput, times(1)).unfollowUser(followEntity);
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
        verify(followPersistenceOutput, never()).unfollowUser(any(FollowEntity.class));
    }
    @Test
    void getFollowing_Success() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1L);
        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollowed(new UserEntity());
        when(followPersistenceOutput.getFollowing(user.getId())).thenReturn(List.of(followEntity));
        when(modelMapper.map(any(UserEntity.class), eq(UserResponse.class))).thenReturn(new UserResponse());

        // Act
        List<UserResponse> following = followService.getFollowing(user);

        // Assert
        assertNotNull(following);
        assertEquals(1, following.size());
        verify(modelMapper, times(1)).map(any(UserEntity.class), eq(UserResponse.class));
    }
    @Test
    void getFollowers_Success() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1L);
        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(new UserEntity());
        when(followPersistenceOutput.getFollowers(user.getId())).thenReturn(List.of(followEntity));
        when(modelMapper.map(any(UserEntity.class), eq(UserResponse.class))).thenReturn(new UserResponse());

        // Act
        List<UserResponse> followers = followService.getFollowers(user);

        // Assert
        assertNotNull(followers);
        assertEquals(1, followers.size());
        verify(modelMapper, times(1)).map(any(UserEntity.class), eq(UserResponse.class));
    }
}
