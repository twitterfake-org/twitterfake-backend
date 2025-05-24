package dev.arack.twitterfake.iam.application.core;

import dev.arack.twitterfake.DataProvider;
import dev.arack.twitterfake.iam.infrastructure.dto.response.UserResponse;
import dev.arack.twitterfake.iam.application.facade.AuthenticationFacade;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import dev.arack.twitterfake.iam.infrastructure.repository.UserRepository;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Test
    void testGetAllUsers() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        List<UserEntity> userEntities = List.of(userEntity);
        when(userRepository.findAll()).thenReturn(userEntities);

        // Act
        List<UserResponse> result = userServiceImpl.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        UserResponse result = userServiceImpl.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findUserEntityByUsername(username);
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.getUserByUsername(username);
        });
        verify(userRepository, times(1)).findUserEntityByUsername(username);
    }

    @Test
    void testUpdateUsername() {
        // Arrange
        Long userId = 1L;
        String newUsername = "newUsername";

        UserEntity userEntity = DataProvider.userEntityMock();
        userEntity.setUsername("oldUsername");

        when(userRepository.existsByUsername(newUsername)).thenReturn(false);
        when(authenticationFacade.getCurrentUser()).thenReturn(userEntity);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        userServiceImpl.updateUsername(newUsername);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(userEntity);

        assertEquals(newUsername, userEntity.getUsername());
    }


    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;
        UserEntity userEntity = DataProvider.userEntityMock();
        when(authenticationFacade.getCurrentUser()).thenReturn(userEntity);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

//        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        // Act
        userServiceImpl.deleteUser();

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(userEntity);
    }
}

