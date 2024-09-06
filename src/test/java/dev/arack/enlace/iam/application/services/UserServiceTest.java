package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.UserPersistenceOutput;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserPersistenceOutput userPersistenceOutput;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetAllUsers() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();
        List<UserEntity> userEntities = List.of(userEntity);
        List<UserResponse> userResponses = List.of(userResponse);
        when(userPersistenceOutput.findAll()).thenReturn(userEntities);
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);

        // Act
        List<UserResponse> result = userService.getAllUsers();

        // Assert
        assertEquals(userResponses, result);
        verify(userPersistenceOutput, times(1)).findAll();
        verify(modelMapper, times(1)).map(userEntity, UserResponse.class);
    }
    @Test
    public void testGetUserByUsername() {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();
        when(userPersistenceOutput.findByUsername(username)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.getUserByUsername(username);

        // Assert
        assertEquals(userResponse, result);
        verify(userPersistenceOutput, times(1)).findByUsername(username);
        verify(modelMapper, times(1)).map(userEntity, UserResponse.class);
    }
    @Test
    public void testUpdateUser() {
        // Arrange
        String username = "testUser";
        UserRequest userRequest = new UserRequest();
        UserEntity userEntity = new UserEntity();
        String encodedPassword = "encodedPassword";
        when(userPersistenceOutput.findByUsername(username)).thenReturn(userEntity);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn(encodedPassword);

        // Act
        userService.updateUser(username, userRequest);

        // Assert
        verify(userPersistenceOutput, times(1)).findByUsername(username);
        verify(modelMapper, times(1)).map(userRequest, userEntity);
        verify(passwordEncoder, times(1)).encode(userRequest.getPassword());
        verify(userPersistenceOutput, times(1)).updateUser(userEntity);
        assertEquals(encodedPassword, userEntity.getPassword());
    }
    @Test
    public void testDeleteUser() {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        when(userPersistenceOutput.findByUsername(username)).thenReturn(userEntity);

        // Act
        userService.deleteUser(username);

        // Assert
        verify(userPersistenceOutput, times(1)).findByUsername(username);
        verify(userPersistenceOutput, times(1)).deleteUser(userEntity);
    }
}
