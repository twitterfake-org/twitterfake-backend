package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.AuthPersistencePort;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.LoginRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.AuthResponse;
import dev.arack.enlace.shared.infrastructure.jwt.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
//    private AuthService authService;
    @Mock
    private AuthPersistencePort authPersistencePort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private Authentication authentication;

    @Test
    void testLogin() {
        // Arrange
//        LoginRequest loginRequest;
//        loginRequest.username("testUser");
//        loginRequest.password("password123");
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//        when(authentication.getName()).thenReturn("testUser");
//        when(jwtUtil.generateAccessToken("testUser")).thenReturn("mockedToken");
//
//        // Act
//        AuthResponse response = authService.login(loginRequest);
//
//        // Assert
//        assertEquals("mockedToken", response.getToken());
//        assertEquals("testUser", response.getUsername());
//        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(jwtUtil, times(1)).generateAccessToken("testUser");
    }

    @Test
    void testRegister() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        String rawPassword = "newPassword";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Act
//        authService.register(userEntity, rawPassword);

        // Assert
        assertEquals(encodedPassword, userEntity.getPassword());
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(authPersistencePort, times(1)).register(userEntity);
    }
}
