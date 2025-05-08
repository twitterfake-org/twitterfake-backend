package dev.arack.twitterfake.iam.infrastructure.controllers.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import dev.arack.twitterfake.iam.application.core.components.GoogleTokenVerifier;
import dev.arack.twitterfake.iam.application.dto.request.GoogleTokenRequest;
import dev.arack.twitterfake.iam.application.dto.request.LoginRequest;
import dev.arack.twitterfake.iam.application.dto.request.SignupRequest;
import dev.arack.twitterfake.iam.application.core.managers.AuthServiceManager;
import dev.arack.twitterfake.iam.application.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.application.dto.response.AuthResponse;
import dev.arack.twitterfake.iam.application.dto.response.GoogleTokenResponseDto;
import dev.arack.twitterfake.shared.configs.AppProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Auth Controller", description = "API for authentication operations")
public class AuthRestController {

    private final AuthServiceManager authServiceManager;

    @Transactional
    @PostMapping(value = "/sign-up")
    @Operation(
            summary = "Sign up a new user",
            description = "Sign up a new user by providing the user's details"
    )
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authServiceManager.signup(request));
    }

    @Transactional
    @PostMapping(value = "/log-in")
    @Operation(
            summary = "Log in a user",
            description = "Log in a user by providing the user's credentials"
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authServiceManager.login(request));
    }

    @Transactional
    @PostMapping(value = "/guest")
    @Operation(
            summary = "Log in as a guest",
            description = "Log in as a guest user"
    )
    public ResponseEntity<AuthResponse> guestLogin() {
        return ResponseEntity.status(HttpStatus.OK).body(authServiceManager.guest());
    }

    @Transactional
    @PostMapping(value = "/log-out")
    @Operation(
            summary = "Log out a user",
            description = "Log out a user"
    )
    public ResponseEntity<Void> logout() {
        authServiceManager.logout();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
