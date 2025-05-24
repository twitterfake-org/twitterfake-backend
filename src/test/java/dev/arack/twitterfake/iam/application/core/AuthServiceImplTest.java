package dev.arack.twitterfake.iam.application.core;

import dev.arack.twitterfake.DataProvider;
import dev.arack.twitterfake.iam.infrastructure.dto.request.LoginRequest;
import dev.arack.twitterfake.iam.infrastructure.dto.request.SignupRequest;
import dev.arack.twitterfake.iam.infrastructure.dto.response.AuthResponse;
import dev.arack.twitterfake.iam.domain.services.UserService;
import dev.arack.twitterfake.iam.infrastructure.utils.TokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private TokenUtil tokenUtil;

    @Test
    void testSignup() {
        // Arrange
        SignupRequest userRequest = new SignupRequest("firstname", "lastname", "username", "password");
        String username = userRequest.username();
        String encodedPassword = "encodedPassword";
        userRequest = userRequest.withPasswordEncoded(encodedPassword);

        when(passwordEncoder.encode(userRequest.password())).thenReturn(encodedPassword);
        when(userService.loadUserByUsername(username)).thenReturn(DataProvider.userDetailsMock());
        when(passwordEncoder.matches(userRequest.password(), encodedPassword)).thenReturn(true);
        when(tokenUtil.generateToken(any(Authentication.class))).thenReturn("token");

        // Act
        AuthResponse authResponse = authService.signup(userRequest);

        // Assert
        assertNotNull(authResponse);
        assertEquals(username, authResponse.username());
        assertEquals("User created successfully", authResponse.message());
        assertTrue(authResponse.status());

        verify(passwordEncoder, times(1)).encode(userRequest.password());
//        verify(userService, times(1)) .createUser(userRequest, RoleEnum.USER);
        verify(userService, times(1)).loadUserByUsername(username);
        verify(tokenUtil, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void testLogin() {
        // Arrange
        LoginRequest userRequest = new LoginRequest("username", "password");
        String username = userRequest.username();
        String encodedPassword = "encodedPassword";

        when(userService.loadUserByUsername(username)).thenReturn(DataProvider.userDetailsMock());
        when(passwordEncoder.matches(userRequest.password(), encodedPassword)).thenReturn(true);
        when(tokenUtil.generateToken(any(Authentication.class))).thenReturn("token");

        // Act
        AuthResponse authResponse = authService.login(userRequest);

        // Assert
        assertNotNull(authResponse);
        assertEquals(username, authResponse.username());
        assertEquals("User logged successfully", authResponse.message());
        assertTrue(authResponse.status());

        verify(userService, times(1)).loadUserByUsername(username);
        verify(passwordEncoder, times(1)).matches(userRequest.password(), encodedPassword);
        verify(tokenUtil, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void testLogout() {
        // Act
        authService.logout();

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}