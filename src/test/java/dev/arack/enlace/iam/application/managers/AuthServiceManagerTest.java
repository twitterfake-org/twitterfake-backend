package dev.arack.enlace.iam.application.managers;

import dev.arack.enlace.DataProvider;
import dev.arack.enlace.iam.application.dto.request.UserRequest;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;
import dev.arack.enlace.iam.application.core.managers.AuthServiceManager;
import dev.arack.enlace.iam.application.port.input.services.UserService;
import dev.arack.enlace.iam.application.port.output.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceManagerTest {

    @InjectMocks
    private AuthServiceManager authService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private TokenUtil tokenUtil;

    @Test
    void testSignup() {
        // Arrange
        UserRequest userRequest = new UserRequest("username", "password");
        String username = userRequest.username();
        String encodedPassword = "encodedPassword";

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
        verify(userService, times(1)).createUser(username, encodedPassword);
        verify(userService, times(1)).loadUserByUsername(username);
        verify(tokenUtil, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void testLogin() {
        // Arrange
        UserRequest userRequest = new UserRequest("username", "password");
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
    void testGuestLogin() {
        // Arrange
        UserDetails guestUser = DataProvider.userDetailsMock();
        when(userService.loadGuestUser()).thenReturn(guestUser);
        when(tokenUtil.generateToken(any(Authentication.class))).thenReturn("guestToken");

        // Act
        AuthResponse authResponse = authService.guestLogin();

        // Assert
        assertNotNull(authResponse);
        assertEquals("guest", authResponse.username());
        assertEquals("Guest logged successfully", authResponse.message());
        assertTrue(authResponse.status());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(guestUser, authentication.getPrincipal());

        verify(userService, times(1)).loadGuestUser();
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