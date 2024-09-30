package dev.arack.enlace.iam.infrastructure.jwt.utils;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;

public interface JwtUtil {
    String generateToken(Authentication authentication);
    DecodedJWT validateToken(String token);
    String extractUsername(DecodedJWT DecodedJWT);
    Claim getSpecificClaim(DecodedJWT DecodedJWT, String claimName);
}
