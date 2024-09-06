package dev.arack.enlace.timeline.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import dev.arack.enlace.timeline.application.ports.input.FollowServiceInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowControllerTest {
    @InjectMocks
    private FollowController followController;
    @Mock
    private FollowServiceInput followServiceInput;
    @Mock
    private UserRepository userRepository;

    @Test
    void unfollowUser_shouldThrowIllegalArgumentException_whenFollowerIsTheSameAsFollowed() {
        Long followerId = 1L;
        Long followedId = 1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followController.unfollowUser(followerId, followedId);
        });
        assertEquals("Cannot unfollow yourself", exception.getMessage());
        verify(followServiceInput, never()).unfollowUser(any(Long.class), any(Long.class));
    }
    @Test
    void followUser_shouldThrowIllegalArgumentException_whenFollowerIsTheSameAsFollowed() {
        Long followerId = 1L;
        Long followedId = 1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followController.followUser(followerId, followedId);
        });
        assertEquals("Cannot follow yourself", exception.getMessage());
        verify(followServiceInput, never()).followUser(any(UserEntity.class), any(UserEntity.class));
    }
    @Test
    void unfollowUser_shouldThrowResourceNotFoundException_whenFollowedUserNotFound() {
        Long followerId = 1L;
        Long followedId = 2L;
        UserEntity follower = new UserEntity();
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followedId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            followController.unfollowUser(followerId, followedId);
        });
        assertEquals("Followed user not found", exception.getMessage());
        verify(followServiceInput, never()).unfollowUser(any(Long.class), any(Long.class));
    }
    @Test
    void unfollowUser_shouldThrowResourceNotFoundException_whenFollowerNotFound() {
        Long followerId = 1L;
        Long followedId = 2L;
        UserEntity follower = new UserEntity();
        lenient().when(userRepository.findById(followerId)).thenReturn(Optional.empty());
        lenient().when(userRepository.findById(followedId)).thenReturn(Optional.of(follower));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            followController.unfollowUser(followerId, followedId);
        });
        assertEquals("Follower not found", exception.getMessage());
        verify(followServiceInput, never()).unfollowUser(any(Long.class), any(Long.class));
    }
    @Test
    void followUser_shouldThrowResourceNotFoundException_whenFollowedUserNotFound() {
        Long followerId = 1L;
        Long followedId = 2L;
        UserEntity follower = new UserEntity();
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followedId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            followController.followUser(followerId, followedId);
        });
        assertEquals("Followed user not found", exception.getMessage());
        verify(followServiceInput, never()).followUser(any(UserEntity.class), any(UserEntity.class));
    }
    @Test
    void followUser_shouldReturnCreated_whenSuccessful() {
        Long followerId = 1L;
        Long followedId = 2L;
        UserEntity follower = new UserEntity();
        UserEntity followed = new UserEntity();
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followedId)).thenReturn(Optional.of(followed));

        ResponseEntity<String> response = followController.followUser(followerId, followedId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User followed successfully", response.getBody());
        verify(followServiceInput, times(1)).followUser(follower, followed);
    }
    @Test
    void followUser_shouldThrowException_whenUserNotFound() {
        Long followerId = 1L;
        Long followedId = 2L;
        when(userRepository.findById(followerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            followController.followUser(followerId, followedId);
        });
        verify(followServiceInput, never()).followUser(any(), any());
    }
    @Test
    void unfollowUser_shouldReturnOk_whenSuccessful() {
        Long followerId = 1L;
        Long followedId = 2L;
        UserEntity follower = new UserEntity();
        UserEntity followed = new UserEntity();
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followedId)).thenReturn(Optional.of(followed));

        ResponseEntity<String> response = followController.unfollowUser(followerId, followedId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User unfollowed successfully", response.getBody());
        verify(followServiceInput, times(1)).unfollowUser(follower.getId(), followed.getId());
    }
    @Test
    void getFollowers_shouldReturnListOfFollowers_whenSuccessful() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        List<UserResponse> followers = Arrays.asList(new UserResponse(), new UserResponse());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(followServiceInput.getFollowers(user)).thenReturn(followers);

        ResponseEntity<List<UserResponse>> response = followController.getFollowers(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(followers, response.getBody());
        verify(followServiceInput, times(1)).getFollowers(user);
    }
    @Test
    void getFollowers_shouldThrowException_whenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            followController.getFollowers(userId);
        });
        verify(followServiceInput, never()).getFollowers(any());
    }
    @Test
    void getFollowing_shouldReturnListOfFollowing_whenSuccessful() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        List<UserResponse> following = Arrays.asList(new UserResponse(), new UserResponse());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(followServiceInput.getFollowing(user)).thenReturn(following);

        ResponseEntity<List<UserResponse>> response = followController.getFollowing(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(following, response.getBody());
        verify(followServiceInput, times(1)).getFollowing(user);
    }
    @Test
    void getFollowing_shouldThrowResourceNotFoundException_whenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            followController.getFollowing(userId);
        });
        assertEquals("User not found", exception.getMessage());
        verify(followServiceInput, never()).getFollowing(any(UserEntity.class));
    }
}
