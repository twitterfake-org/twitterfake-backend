package dev.arack.twitterfake.iam.infrastructure.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import dev.arack.twitterfake.iam.domain.services.AuthService;
import dev.arack.twitterfake.iam.infrastructure.security.components.GoogleTokenVerifier;
import dev.arack.twitterfake.iam.infrastructure.dto.request.CodeRequest;
import dev.arack.twitterfake.iam.infrastructure.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.infrastructure.dto.response.AuthResponse;
import dev.arack.twitterfake.shared.configs.AppProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/oauth2", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "oAuth2 Controller", description = "API for authentication operations")
public class Oauth2Controller {

    private final AuthService authService;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final AppProperties appProperties;

    @PostMapping("/google/callback")
    public ResponseEntity<AuthResponse> handleGoogleToken(@RequestBody CodeRequest request) {
        String code = request.code();
        Map<String, Object> tokenResponse = googleTokenVerifier.exchangeCodeForTokens(code);
        String idToken = (String) tokenResponse.get("id_token");
        log.info("Google token exchange successful. ID Token: {}", idToken);

        return googleTokenVerifier.verify(idToken)
                .map(payload -> {
                    SocialRequest socialRequest = buildSocialRequestFromPayload(payload);
                    AuthResponse authResponse = authService.continueWithGoogle(socialRequest);
                    return ResponseEntity.ok(authResponse);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        AuthResponse.createInvalidAuthResponse()
                ));
    }

    @GetMapping("/callback/google")
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
                        AuthResponse authResponse = authService.continueWithGoogle(socialRequest);

                        String redirectUrl = UriComponentsBuilder
                                .fromHttpUrl(appProperties.getFrontendUrl())
                                .path("/oauth2/callback")
                                .queryParam("token", authResponse.token())
                                .queryParam("state", state)
                                .build()
                                .toUriString();

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
