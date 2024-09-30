//package dev.arack.enlace.profile.application.managers;
//
//
//import dev.arack.enlace.iam.domain.aggregates.UserEntity;
//import dev.arack.enlace.iam.infrastructure.repository.UserJpaRepository;
//import dev.arack.enlace.profile.application.port.output.persistence.ConnectionPersistence;
//import dev.arack.enlace.profile.domain.entity.ConnectionEntity;
//import dev.arack.enlace.profile.infrastructure.repository.ConnectionJpaRepository;
//import jakarta.validation.ValidationException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ConnectionServiceManagerTest {
//    @InjectMocks
//    private ConnectionServiceManager customConnectionUsecase;
//    @Mock
//    private ConnectionJpaRepository connectionJpaRepository;
//    @Mock
//    private ConnectionPersistence connectionPersistence;
//    @Mock
//    private UserJpaRepository UserJpaRepository;
//
//    @Test
//    void followUser_Success() {
//        // Arrange
//        Long followerId = 1L;
//        Long followedId = 2L;
//        UserEntity follower = new UserEntity();
//        UserEntity followed = new UserEntity();
//        follower.setId(followerId);
//        followed.setId(followedId);
//        when(UserJpaRepository.findById(followerId)).thenReturn(Optional.of(follower));
//        when(UserJpaRepository.findById(followedId)).thenReturn(Optional.of(followed));
//        when(connectionJpaRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(false);
//
//        // Act
//        customConnectionUsecase.followUser(followerId, followedId);
//
//        // Assert
//        verify(connectionPersistence, times(1)).followUser(any(ConnectionEntity.class));
//    }
//
//    @Test
//    void followUser_AlreadyFollowing() {
//        // Arrange
//        Long followerId = 1L;
//        Long followedId = 2L;
//        UserEntity follower = new UserEntity();
//        UserEntity followed = new UserEntity();
//        follower.setId(followerId);
//        followed.setId(followedId);
//        when(UserJpaRepository.findById(followerId)).thenReturn(Optional.of(follower));
//        when(UserJpaRepository.findById(followedId)).thenReturn(Optional.of(followed));
//        when(connectionJpaRepository.existsByFollowerAndFollowed(follower, followed)).thenReturn(true);
//
//        // Act & Assert
//        Exception exception = assertThrows(ValidationException.class, () -> {
//            customConnectionUsecase.followUser(followerId, followedId);
//        });
//        assertEquals("Already following this user", exception.getMessage());
//        verify(connectionPersistence, never()).followUser(any(ConnectionEntity.class));
//    }
//
//    @Test
//    void unfollowUser_Success() {
//        // Arrange
//        Long followerId = 1L;
//        Long followedId = 2L;
//        ConnectionEntity connectionEntity = new ConnectionEntity();
//        when(connectionJpaRepository.findByFollowerIdAndFollowedId(followerId, followedId)).thenReturn(Optional.of(connectionEntity));
//
//        // Act
//        customConnectionUsecase.unfollowUser(followerId, followedId);
//
//        // Assert
//        verify(connectionPersistence, times(1)).unfollowUser(connectionEntity);
//    }
//
//    @Test
//    void unfollowUser_NotFollowing() {
//        // Arrange
//        Long followerId = 1L;
//        Long followedId = 2L;
//        when(connectionJpaRepository.findByFollowerIdAndFollowedId(followerId, followedId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            customConnectionUsecase.unfollowUser(followerId, followedId);
//        });
//        assertEquals("Follow relationship does not exist", exception.getMessage());
//        verify(connectionPersistence, never()).unfollowUser(any(ConnectionEntity.class));
//    }
//
//    @Test
//    void getFollowing_Success() {
//        // Arrange
//        Long userId = 1L;
//        ConnectionEntity connectionEntity = new ConnectionEntity();
//        when(connectionPersistence.getFollowing(userId)).thenReturn(List.of(connectionEntity));
//
//        // Act
////        List<FollowEntity> following = followService.getFollowing(userId);
//
//        // Assert
////        assertNotNull(following);
////        assertEquals(1, following.size());
//    }
//
//    @Test
//    void getFollowers_Success() {
//        // Arrange
//        Long userId = 1L;
//        ConnectionEntity connectionEntity = new ConnectionEntity();
//        when(connectionPersistence.getFollowers(userId)).thenReturn(List.of(connectionEntity));
//
//        // Act
////        List<FollowEntity> followers = followService.getFollowers(userId);
////
////        // Assert
////        assertNotNull(followers);
////        assertEquals(1, followers.size());
//    }
//}
