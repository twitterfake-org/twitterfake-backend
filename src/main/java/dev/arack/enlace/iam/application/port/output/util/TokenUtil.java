package dev.arack.enlace.iam.application.port.output.util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;

public interface TokenUtil {
    String generateToken(Authentication authentication);
    DecodedJWT validateToken(String token);
    String extractUsername(DecodedJWT decodedJWT);
    Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName);
}
