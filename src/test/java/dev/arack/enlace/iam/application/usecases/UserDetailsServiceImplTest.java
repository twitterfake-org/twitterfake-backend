package dev.arack.enlace.iam.application.usecases;

import dev.arack.enlace.iam.application.port.output.persistence.UserPersistencePort;
import dev.arack.enlace.iam.domain.aggregate.UserEntity;
import dev.arack.enlace.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private UserPersistencePort UserPersistencePort;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPassword");
//        userEntity.setRole(RoleEnum.USER);

//        when(IUserPersistencePort.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("testUser");

        // Assert
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
        SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        assertEquals(Collections.singleton(expectedAuthority), userDetails.getAuthorities());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
//        when(IUserPersistencePort.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("nonExistentUser");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testGetCurrentUser_Authenticated() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        UserDetails userDetails = new User("testUser", "testPassword", Collections.emptyList());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(IUserPersistencePort.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        SecurityContextHolder.setContext(securityContext);

        // Act
        UserEntity currentUser = userDetailsServiceImpl.getCurrentUser();

        // Assert
        assertNotNull(currentUser);
        assertEquals("testUser", currentUser.getUsername());
    }

    @Test
    public void testGetCurrentUser_NotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UserEntity currentUser = userDetailsServiceImpl.getCurrentUser();

        // Assert
        assertNull(currentUser);
    }

    @Test
    public void testGetCurrentUser_NoAuthentication() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UserEntity currentUser = userDetailsServiceImpl.getCurrentUser();

        // Assert
        assertNull(currentUser);
    }
}
