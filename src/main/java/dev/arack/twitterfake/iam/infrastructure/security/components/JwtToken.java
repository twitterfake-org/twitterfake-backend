package dev.arack.twitterfake.iam.infrastructure.security.components;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;

public interface JwtToken {
    String generateToken(Authentication authentication);
    DecodedJWT validateToken(String token);
    String extractUsername(DecodedJWT decodedJWT);
    Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName);
}
