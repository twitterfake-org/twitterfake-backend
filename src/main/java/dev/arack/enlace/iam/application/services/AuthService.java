package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.AuthPersistencePort;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.application.ports.input.AuthServicePort;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.LoginRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.AuthResponse;
import dev.arack.enlace.shared.infrastructure.jwt.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServicePort {

    private final AuthPersistencePort authPersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authToken);

        String token = jwtUtil.generateAccessToken(authentication.getName());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUsername(authentication.getName());

        return authResponse;
    }

    @Override
    public void register(UserEntity userEntity, String password) {
        userEntity.setPassword(passwordEncoder.encode(password));

        authPersistencePort.register(userEntity);
    }
}
