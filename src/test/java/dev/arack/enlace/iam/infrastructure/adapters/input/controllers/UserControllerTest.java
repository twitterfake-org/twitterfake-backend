package dev.arack.enlace.iam.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.application.ports.input.UserServiceInput;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserServiceInput userServiceInput;

    @Test
    public void testGetAllUsers() {
        // Arrange
        UserResponse userResponse = new UserResponse();
        List<UserResponse> userResponses = List.of(userResponse);
        when(userServiceInput.getAllUsers()).thenReturn(userResponses);

        // Act
        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponses, response.getBody());
        verify(userServiceInput, times(1)).getAllUsers();
    }
    @Test
    public void testGetUserByUsername() {
        // Arrange
        String username = "testUser";
        UserResponse userResponse = new UserResponse();
        when(userServiceInput.getUserByUsername(username)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.getUserByUsername(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userServiceInput, times(1)).getUserByUsername(username);
    }
    @Test
    public void testUpdateUser() {
        // Arrange
        String username = "testUser";
        UserRequest userRequest = new UserRequest();

        // Act
        ResponseEntity<Void> response = userController.updateUser(username, userRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userServiceInput, times(1)).updateUser(username, userRequest);
    }
    @Test
    public void testDeleteUser() {
        // Arrange
        String username = "testUser";

        // Act
        ResponseEntity<Void> response = userController.deleteUser(username);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userServiceInput, times(1)).deleteUser(username);
    }
}
