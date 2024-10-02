package dev.arack.enlace.iam.application.internal.managers;

import dev.arack.enlace.iam.application.dto.request.UserRequest;
import dev.arack.enlace.iam.application.services.AuthService;
import dev.arack.enlace.iam.application.services.UserService;
import dev.arack.enlace.iam.application.dto.request.LoginRequest;
import dev.arack.enlace.iam.application.dto.request.SignupRequest;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;
import dev.arack.enlace.iam.application.port.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthManager implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthResponse signup(UserRequest signupRequest) {
        String username = signupRequest.username();
        String password = signupRequest.password();

        userService.createUser(username, passwordEncoder.encode(password));
        Authentication authentication = this.authenticate(username, password);
        String accessToken = jwtUtil.generateToken(authentication);

        return new AuthResponse(username, "User created successfully", true, accessToken);
    }

    public AuthResponse login(UserRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        String accessToken = jwtUtil.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthResponse(username, "User logged successfully", true, accessToken);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}