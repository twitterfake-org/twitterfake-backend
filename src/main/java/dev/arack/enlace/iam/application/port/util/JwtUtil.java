package dev.arack.enlace.iam.application.port.util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUtil {
    String generateToken(Authentication authentication);
    DecodedJWT validateToken(String token);
    String extractUsername(DecodedJWT DecodedJWT);
    Claim getSpecificClaim(DecodedJWT DecodedJWT, String claimName);

//    UserDetails extractUserDetails(DecodedJWT decodedJWT);
}
