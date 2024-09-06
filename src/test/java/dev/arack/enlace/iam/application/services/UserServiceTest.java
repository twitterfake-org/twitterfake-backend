package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.UserPersistencePort;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserPersistencePort userPersistencePort;

    @Test
    public void testGetAllUsers() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        List<UserEntity> userEntities = List.of(userEntity);
        when(userPersistencePort.findAll()).thenReturn(userEntities);

        // Act
        List<UserEntity> result = userService.getAllUsers();

        // Assert
        assertEquals(userEntities, result);
        verify(userPersistencePort, times(1)).findAll();
    }
    @Test
    public void testGetUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        when(userPersistencePort.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        UserEntity result = userService.getUserByUsername(username);

        // Assert
        assertEquals(userEntity, result);
        verify(userPersistencePort, times(1)).findByUsername(username);
    }
    @Test
    public void testGetUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userPersistencePort.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByUsername(username);
        });
        verify(userPersistencePort, times(1)).findByUsername(username);
    }
    @Test
    public void testUpdateUser() {
        // Arrange
        String username = "testUser";
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("John");
        userRequest.setLastName("Doe");

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("OldFirstName");
        userEntity.setLastName("OldLastName");

        when(userPersistencePort.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        userService.updateUser(username, userRequest);

        // Assert
        verify(userPersistencePort, times(1)).findByUsername(username);
        verify(userPersistencePort, times(1)).updateUser(userEntity);
        assertEquals("John", userEntity.getFirstName());
        assertEquals("Doe", userEntity.getLastName());
    }
    @Test
    public void testDeleteUser() {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        when(userPersistencePort.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        userService.deleteUser(username);

        // Assert
        verify(userPersistencePort, times(1)).findByUsername(username);
        verify(userPersistencePort, times(1)).deleteUser(userEntity);
    }
}
