package dev.arack.twitterfake.iam.infrastructure.security.components;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.util.Map;
import java.util.Optional;

public interface GoogleTokenVerifier {

    Optional<GoogleIdToken.Payload> verify(String idToken);

    Map<String, Object> exchangeCodeForTokens(String code);
}
