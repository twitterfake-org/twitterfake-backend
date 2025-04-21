package dev.arack.enlace.iam.infrastructure.controllers.rest;

import dev.arack.enlace.iam.application.dto.request.LoginRequest;
import dev.arack.enlace.iam.application.dto.request.SignupRequest;
import dev.arack.enlace.iam.application.core.managers.AuthServiceManager;
import dev.arack.enlace.iam.application.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
