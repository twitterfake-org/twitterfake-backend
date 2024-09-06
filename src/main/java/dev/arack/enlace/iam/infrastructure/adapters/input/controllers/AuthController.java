package dev.arack.enlace.iam.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.application.ports.input.AuthServiceInput;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.LoginRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.RegisterRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Auth Controller", description = "API for authentication operations")
public class AuthController {

    private final AuthServiceInput authServiceInput;

    @PostMapping(value = "/login")
    @Operation(
            summary = "Login",
            description = "Login by providing the username and password"
    )
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authServiceInput.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = "/register")
    @Operation(
            summary = "Register",
            description = "Register by providing the first name, last name, username, and password"
    )
    public ResponseEntity<String> register(@RequestBody RegisterRequest authRequest) {
        authServiceInput.register(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}
