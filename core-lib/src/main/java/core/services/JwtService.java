package core.services;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import core.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtService {

    private final SecretKey key;
    private final Long EXPIRATION_DELAY = 3600L;

    public JwtService() {
        if (System.getenv("JWT_KEY").isBlank() || System.getenv("JWT_KEY").trim().length() < 32) {
            throw new IllegalStateException("JwtService instance creation: ERROR 01: missing or bad JWT_KEY env variable");
        }
        byte[] keyBytes = Decoders.BASE64.decode(System.getenv("JWT_KEY"));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate a token
     * 
     * @param claims example : {companyId: companyIdValue, userId: userIdValue}
     * @return JWT in utf-8 string format
     */
    public String generateToken(Map<String, String> claims) {
        Long expiration = System.currentTimeMillis() + this.EXPIRATION_DELAY * 1000;

        return Jwts.builder().claims(claims).expiration(new Date(expiration)).signWith(this.key).compact();
    }

    /**
     * Decode a token. Throws custom InvalidTokenException if token is invalid or expired
     * 
     * @param token : jwt
     * @return claims (object we encoded with generateToken method)
     */
    public Map<String, Object> decodeToken(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(this.key).build();

        try {
            return (Claims) jwtParser.parse(token).getPayload();
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }
}
