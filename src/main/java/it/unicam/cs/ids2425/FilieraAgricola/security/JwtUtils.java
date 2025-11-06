package it.unicam.cs.ids2425.FilieraAgricola.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List; // Importa List

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${filiera.jwt.secret}")
    private String jwtSecret;

    @Value("${filiera.jwt.expirationMs}")
    private int jwtExpirationMs;

    private Key getSigningKey() {
        // Genera una chiave sicura basata sul secret
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // --- MODIFICATO ---
    // Il metodo ora accetta 'List<String> ruoli' invece di 'String ruolo'
    public String generateToken(String email, List<String> ruoli) {
        return Jwts.builder()
                .setSubject(email)
                // --- MODIFICATO ---
                // Il claim ora è "ruoli" (plurale) e accetta la lista
                .claim("ruoli", ruoli)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT non valido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT scaduto: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT non supportato: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Payload del token JWT vuoto: {}", e.getMessage());
        }
        return false;
    }
}

