package com.login.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class Jwtutil {
    // Token validity period (in milliseconds), here set to 1 hour.
    public static final long JWT_TOKEN_VALIDITY = 3_600_000;

    // Secret key for signing the JWT (should ideally be stored securely in environment variables).
    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    // Retrieve the username (subject) from the JWT token.
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve the expiration date from the JWT token.
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Retrieve specific claim from the token using a resolver function.
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Retrieve all claims from the JWT token (requires the secret key).
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    // Check if the token has expired.
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate a token with custom claims (e.g., role, email, ID).
    public String generateToken(String userName, String role, int id, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("email", email);
        claims.put("id", id);
        return doGenerateToken(claims, userName);
    }

    // Create and sign the token with claims, subject, and expiration.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                   .setClaims(claims) // Set additional claims.
                   .setSubject(subject) // Set the subject (username).
                   .setIssuedAt(new Date(System.currentTimeMillis())) // Token creation timestamp.
                   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // Token expiry.
                   .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), SignatureAlgorithm.HS512) // Sign with secret and algorithm.
                   .compact(); // Serialize to a compact URL-safe string.
    }

    // Validate the JWT token and ensure it is properly signed.
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .build()
                .parseClaimsJws(token);
        } catch (JwtException e) {
            // Re-throwing the exception to indicate validation failure.
            throw e;
        }
    }
}