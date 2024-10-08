package dev.arack.enlace.iam.application.core.managers;

import dev.arack.enlace.iam.application.dto.request.UserRequest;
import dev.arack.enlace.iam.application.port.input.services.AuthService;
import dev.arack.enlace.iam.application.port.input.services.UserService;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;
import dev.arack.enlace.iam.application.port.output.util.TokenUtil;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceManager implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokenUtil tokenUtil;

    public AuthResponse signup(UserRequest signupRequest) {
        String username = signupRequest.username();
        String password = signupRequest.password();

        userService.createUser(username, passwordEncoder.encode(password), RoleEnum.USER);
        Authentication authentication = this.authenticate(username, password);
        String accessToken = tokenUtil.generateToken(authentication);

        return new AuthResponse(username, "User created successfully", true, accessToken);
    }

    public AuthResponse login(UserRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        String accessToken = tokenUtil.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthResponse(username, "User logged successfully", true, accessToken);
    }

    public AuthResponse guestLogin() {

        String username = "guest#" + UUID.randomUUID();
        String password = UUID.randomUUID().toString();
        userService.createUser(username, passwordEncoder.encode(password), RoleEnum.GUEST);

        return login(new UserRequest(username, password));
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = userService.loadUserByUsername(username);
        log.info("userDetails : {}", userDetails);


        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}