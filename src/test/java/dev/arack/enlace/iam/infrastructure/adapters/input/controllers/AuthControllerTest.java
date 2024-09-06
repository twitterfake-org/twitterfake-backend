package dev.arack.enlace.iam.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.application.ports.input.AuthServiceInput;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.LoginRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.RegisterRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthServiceInput authServiceInput;

    @Test
    void testLogin() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        AuthResponse authResponse = new AuthResponse();
        when(authServiceInput.login(loginRequest)).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(authServiceInput, times(1)).login(loginRequest);
    }
    @Test
    void testRegister() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();

        // Act
        ResponseEntity<String> response = authController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(authServiceInput, times(1)).register(registerRequest);
    }
}
