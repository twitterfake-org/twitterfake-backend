package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.UserPersistenceOutput;
import dev.arack.enlace.iam.domain.model.RoleEnum;
import dev.arack.enlace.iam.domain.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private UserPersistenceOutput userPersistenceOutput;
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
        userEntity.setRole(RoleEnum.USER);
        when(userPersistenceOutput.findByUsername("testUser")).thenReturn(userEntity);

        // Act
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("testUser");

        // Assert
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
        GrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        assertEquals(Collections.singleton(expectedAuthority), userDetails.getAuthorities());
    }
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userPersistenceOutput.findByUsername("nonExistentUser"))
                .thenThrow(new UsernameNotFoundException("User with username nonExistentUser not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("nonExistentUser");
        });
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
        when(userPersistenceOutput.findByUsername("testUser")).thenReturn(userEntity);
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
