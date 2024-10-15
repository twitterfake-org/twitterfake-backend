package dev.arack.enlace.iam.application.core.managers;

import dev.arack.enlace.iam.application.dto.request.LoginRequest;
import dev.arack.enlace.iam.application.dto.request.SignupRequest;
import dev.arack.enlace.iam.application.port.input.services.AuthService;
import dev.arack.enlace.iam.application.port.input.services.UserService;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;
import dev.arack.enlace.iam.application.port.output.util.TokenUtil;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceManager implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokenUtil tokenUtil;

    public AuthResponse signup(SignupRequest signupRequest) {
        try {
            String username = signupRequest.username();
            String password = signupRequest.password();

            // Codificar la contraseña antes de guardar el usuario
            signupRequest = signupRequest.withPasswordEncoded(passwordEncoder.encode(password));
            log.info("Saving new user with username: {}", username);

            userService.createUser(signupRequest, RoleEnum.USER);
            log.info("User {} saved successfully", username);

            // Autenticar al usuario después de la creación
            Authentication authentication = this.authenticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar token JWT
            String accessToken = tokenUtil.generateToken(authentication);

            return new AuthResponse(username, "User created successfully", true, accessToken);
        } catch (Exception e) {
            log.error("Error during user signup: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during signup");
        }
    }


    public AuthResponse login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        String accessToken = tokenUtil.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthResponse(username, "User logged successfully", true, accessToken);
    }

    public AuthResponse guest() {

        String username = "guest" + UUID.randomUUID();
        String password = UUID.randomUUID().toString();
        SignupRequest signupRequest = new SignupRequest(
                "Guest",
                "User",
                username,
                password
        );
        signupRequest = signupRequest.withPasswordEncoded(passwordEncoder.encode(password));
        userService.createUser(signupRequest, RoleEnum.GUEST);

        return login(new LoginRequest(username, password));
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }


    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}