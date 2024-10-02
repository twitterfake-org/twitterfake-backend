package dev.arack.enlace.iam.application.managers;

import dev.arack.enlace.DataProvider;
import dev.arack.enlace.iam.application.dto.request.SignupRequest;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;
import dev.arack.enlace.iam.application.internal.managers.AuthManager;
import dev.arack.enlace.iam.application.port.persistence.RolePersistence;
import dev.arack.enlace.iam.application.port.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.iam.domain.events.UserCreatedEvent;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import dev.arack.enlace.iam.application.port.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthManagerTest {

    @InjectMocks
    private AuthManager authService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RolePersistence rolePersistence;
    @Mock
    private UserPersistence userPersistence;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void testSignup() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest("username", "password", List.of(RoleEnum.USER));
        String username = signupRequest.username();
        UserEntity userEntity = DataProvider.userEntityMock();

        // Mocking
        // Asegúrate de que el mock devuelva un Optional<UserEntity> vacío para simular que el usuario no existe
        when(userPersistence.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));

        // Mockear roles
        when(rolePersistence.findRoleEntitiesByRoleNameIn(List.of(RoleEnum.USER)))
                .thenReturn(Set.of(DataProvider.roleEntityMock().toArray(new RoleEntity[0]))); // Ajustar para devolver Set<RoleEntity>

        // Mockear el guardado del usuario
        UserEntity mockUserEntity = DataProvider.userEntityMock(); // Usar un password de prueba
        when(userPersistence.save(any(UserEntity.class))).thenReturn(mockUserEntity);

        // Mockear la encriptación de la contraseña
        when(passwordEncoder.encode(signupRequest.password())).thenReturn("encodedPassword");

//        when(authService.loadUserByUsername(username)).thenReturn(any(User.class));

        // Ajustar el stubbing para aceptar cualquier Authentication
        when(jwtUtil.generateToken(any(Authentication.class))).thenReturn("mockedToken");

        // Act
        AuthResponse authResponse = authService.signup(signupRequest);

        // Assert
        assertNotNull(authResponse);
        assertEquals(username, authResponse.username());
        assertEquals("User created successfully", authResponse.message());
        assertTrue(authResponse.status());

        // Verificar interacciones
        verify(passwordEncoder, times(1)).encode(signupRequest.password());
        verify(userPersistence, times(1)).save(any(UserEntity.class));
        verify(eventPublisher, times(1)).publishEvent(any(UserCreatedEvent.class));
        verify(jwtUtil, times(1)).generateToken(any(Authentication.class));
    }
}