package dev.arack.enlace.iam.application.managers;

import dev.arack.enlace.iam.application.port.input.services.AuthService;
import dev.arack.enlace.iam.application.port.input.services.UserService;
import dev.arack.enlace.iam.application.port.output.persistence.RolePersistence;
import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.iam.domain.events.UserCreatedEvent;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.application.dto.request.LoginRequest;
import dev.arack.enlace.iam.application.dto.request.SignupRequest;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;
import dev.arack.enlace.iam.application.port.output.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceManager implements AuthService {

    private final UserPersistence userPersistence;
    private final RolePersistence rolePersistence;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthResponse signup(SignupRequest signupRequest) {
        String username = signupRequest.username();
        String password = signupRequest.password();
        Set<RoleEntity> roles = rolePersistence.findRoleEntitiesByRoleNameIn(signupRequest.roleList());

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist.");
        }
        UserEntity userSaved = userPersistence.save(
                UserEntity.fromUsernamePasswordAndRoles(username, passwordEncoder.encode(password), roles)
        );

        eventPublisher.publishEvent(new UserCreatedEvent(this, userSaved));
        Authentication authentication = authenticate(username, password);
        String accessToken = jwtUtil.generateToken(authentication);

        return new AuthResponse(username, "User created successfully", true, accessToken);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtil.generateToken(authentication);

        return new AuthResponse(username, "User logged successfully", true, accessToken);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.info("User details: {}", userDetails);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}