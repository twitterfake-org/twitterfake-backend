package dev.arack.enlace.iam.application.managers;

import dev.arack.enlace.DataProvider;
import dev.arack.enlace.iam.application.core.managers.UserServiceManager;
import dev.arack.enlace.iam.application.dto.response.UserResponse;
import dev.arack.enlace.iam.application.port.input.facade.AuthenticationFacade;
import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceManagerTest {

    @InjectMocks
    private UserServiceManager userServiceImpl;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Test
    void testGetAllUsers() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        List<UserEntity> userEntities = List.of(userEntity);
        when(userPersistence.findAll()).thenReturn(userEntities);

        // Act
        List<UserResponse> result = userServiceImpl.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        verify(userPersistence, times(1)).findAll();
    }

    @Test
    void testGetUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        when(userPersistence.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        UserResponse result = userServiceImpl.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        verify(userPersistence, times(1)).findUserEntityByUsername(username);
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userPersistence.findUserEntityByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.getUserByUsername(username);
        });
        verify(userPersistence, times(1)).findUserEntityByUsername(username);
    }

    @Test
    void testUpdateUsername() {
        // Arrange
        Long userId = 1L;
        String newUsername = "newUsername";

        UserEntity userEntity = DataProvider.userEntityMock();
        userEntity.setUsername("oldUsername");

        when(userPersistence.existsByUsername(newUsername)).thenReturn(false);
        when(authenticationFacade.getCurrentUser()).thenReturn(userEntity);
        when(userPersistence.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        userServiceImpl.updateUsername(newUsername);

        // Assert
        verify(userPersistence, times(1)).findById(userId);
        verify(userPersistence, times(1)).save(userEntity);

        assertEquals(newUsername, userEntity.getUsername());
    }


    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;
        UserEntity userEntity = DataProvider.userEntityMock();
        when(authenticationFacade.getCurrentUser()).thenReturn(userEntity);
        when(userPersistence.findById(userId)).thenReturn(Optional.of(userEntity));

//        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        // Act
        userServiceImpl.deleteUser();

        // Assert
        verify(userPersistence, times(1)).findById(userId);
        verify(userPersistence, times(1)).deleteUser(userEntity);
    }
}

