package com.leandro.identityAccessService.security.service;

import com.leandro.identityAccessService.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getId())
                .issuedAt(new Date())
                .expiration(
                        Date.from(
                                Instant.now().plus(1, ChronoUnit.DAYS)
                        )
                )
                .signWith(getKey())
                .compact();
    }

    public String extractUserId(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
