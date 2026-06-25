package core.services;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import core.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtService {

    private final SecretKey key;
    private final Long EXPIRATION_DELAY = 3600L;

    public JwtService() {
        String stringKey = System.getenv("JWT_KEY");

        if (stringKey == null || stringKey.isEmpty()) {
            throw new IllegalStateException("JwtService instance creation: ERROR 01: missing JWT_KEY env variable");
        }
        this.key = Keys.hmacShaKeyFor(stringKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a token
     * 
     * @param claims : object with key / values we want to be encoded, example : {companyId: <companyIdValue>, userId: <userIdValue>}
     * @return JWT in utf-8 string format
     */
    public String generateToken(Map<String, String> claims) {
        return Jwts.builder().claims(claims).issuedAt(Date.from(Instant.now())).expiration(Date.from(Instant.now().plusSeconds(EXPIRATION_DELAY)))
                .signWith(this.key).compact();
    }

    /**
     * Decode a token. Throws custom InvalidTokenException if token is invalid or expired. Could throw a JJWT exception but better not for proper
     * seperation of concerns (Spring Boot app must no see which JWT library we use)
     * 
     * @param token : jwt
     * @return claims (object we encoded with generateToken method)
     */
    public Claims decodeToken(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(this.key).build();

        try {
            return (Claims) jwtParser.parse(token).getPayload();
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }
}
