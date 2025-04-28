package dev.arack.twitterfake.iam.application.core.managers;

import dev.arack.twitterfake.iam.application.dto.request.LoginRequest;
import dev.arack.twitterfake.iam.application.dto.request.SignupRequest;
import dev.arack.twitterfake.iam.application.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.application.dto.response.UserResponse;
import dev.arack.twitterfake.iam.application.port.input.services.AuthService;
import dev.arack.twitterfake.iam.application.port.input.services.UserService;
import dev.arack.twitterfake.iam.application.dto.response.AuthResponse;
import dev.arack.twitterfake.iam.application.port.output.persistence.UserPersistence;
import dev.arack.twitterfake.iam.application.port.output.util.TokenUtil;
import dev.arack.twitterfake.iam.domain.valueobject.RoleEnum;
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
    private final UserPersistence userPersistence;

    public AuthResponse signup(SignupRequest signupRequest) {
        String username = signupRequest.username();
        String password = signupRequest.password();

        signupRequest = signupRequest.withPasswordEncoded(passwordEncoder.encode(password));
        SocialRequest socialRequest = new SocialRequest(
                signupRequest.firstName(),
                signupRequest.lastName(),
                signupRequest.username(),
                ""
        );
        userService.createUser(signupRequest, RoleEnum.USER, socialRequest);

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenUtil.generateToken(authentication);

        return new AuthResponse(username, "User created successfully", true, jwt);
    }


    public AuthResponse login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        String jwt = tokenUtil.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthResponse(username, "User logged successfully", true, jwt);
    }

    public AuthResponse guest() {

        String firstName = "Guest";
        String lastName = "User";
        String email = "guest" + UUID.randomUUID().toString().substring(0, 5) + "@example.com";
        SignupRequest request = generateUser(firstName, lastName, email);
        SocialRequest socialRequest = new SocialRequest(firstName, lastName, email, "");
        userService.createUser(request, RoleEnum.GUEST, socialRequest);

        return login(new LoginRequest(request.username(), request.password()));
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public AuthResponse continueWithGoogle(SocialRequest social) {

        String username = social.getEmail().split("@")[0];

        if (!userPersistence.existsByUsername(username)) {
            SignupRequest request = generateUser(social.getFirstName(), social.getLastName(), username);
            userService.createUser(request, RoleEnum.USER, social);
        }
        UserResponse user = userService.getUserByUsername(username);
        UserDetails userDetails = userService.loadUserByUsername(user.username());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenUtil.generateToken(authentication);
        return new AuthResponse(username, "User logged successfully", true, jwt);
    }

    private SignupRequest generateUser(String firstName, String lastName, String email) {

        String username = email.contains("@") ? email.split("@")[0] : email;
        String password = UUID.randomUUID().toString();

        SignupRequest signupRequest = new SignupRequest(firstName, lastName, username, password);
        signupRequest.withPasswordEncoded(passwordEncoder.encode(password));
        return signupRequest;
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}