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

import java.net.URI;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Auth Controller", description = "API for authentication operations")
public class AuthRestController {

    private final AuthServiceManager authServiceManager;
    private final GoogleTokenVerifier googleTokenVerifier;

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

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> handleGoogleToken(@RequestBody GoogleTokenRequest request) {
        String idToken = request.idToken();

        return googleTokenVerifier.verify(idToken)
                .map(payload -> {
                    SocialRequest socialRequest = buildSocialRequestFromPayload(payload);
                    return ResponseEntity.ok(authServiceManager.continueWithGoogle(socialRequest));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        AuthResponse.createInvalidAuthResponse()
                ));
    }

    @GetMapping("/google/callback")
    public ResponseEntity<Object> handleGoogleRedirect(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        try {
            // 1. Aquí podrías opcionalmente validar el "stateFromGoogle" si guardaste el "expectedState" del usuario en backend.
            // En muchos casos sencillos, solo se valida en frontend.

            Map<String, Object> tokenResponse = googleTokenVerifier.exchangeCodeForTokens(code);
            String idToken = (String) tokenResponse.get("id_token");

            return googleTokenVerifier.verify(idToken)
                    .map(payload -> {
                        SocialRequest socialRequest = buildSocialRequestFromPayload(payload);
                        AuthResponse authResponse = authServiceManager.continueWithGoogle(socialRequest);

                        String redirectUrl = "http://localhost:4200/login?token=" + authResponse.token()
                                + "&state=" + state;

                        return ResponseEntity.status(HttpStatus.FOUND)
                                .location(URI.create(redirectUrl))
                                .build();
                    })
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private SocialRequest buildSocialRequestFromPayload(GoogleIdToken.Payload payload) {

        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String photoUrl = (String) payload.get("picture");

        return SocialRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .photoUrl(photoUrl)
                .build();
    }

}
