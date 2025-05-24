package dev.arack.twitterfake.iam.application.components;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class GoogleTokenVerifierUtil {

    private final GoogleIdTokenVerifier verifier;

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    private static final String TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";

    public GoogleTokenVerifierUtil(
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.google.client-secret}") String clientSecret,
            @Value("${spring.security.oauth2.client.registration.google.redirect-uri}") String redirectUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;

        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public Optional<GoogleIdToken.Payload> verify(String idToken) {
        try {
            GoogleIdToken token = verifier.verify(idToken);
            return token != null ? Optional.of(token.getPayload()) : Optional.empty();
        } catch (Exception e) {
            // Agregar log o manejar error
            return Optional.empty();
        }
    }

    public Map<String, Object> exchangeCodeForTokens(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl(TOKEN_ENDPOINT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("grant_type", "authorization_code");

        try {
            return webClient.post()
                    .bodyValue(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new RuntimeException("Failed: " + errorBody)))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block(); // Esperamos la respuesta (modo síncrono)
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while exchanging the code for tokens", e);
        }
    }

}
