package com.example.demo_adv.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo_adv.model.UserRole;
import com.example.demo_adv.model.entity.Advogado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret:my-secret-key-123}")
    private String secret;

    public String generateToken(Advogado advogado) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("demo-adv-api")
                    .withSubject(advogado.getEmail())
                    .withClaim("role", advogado.getRole().name())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("demo-adv-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public UserRole extractRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var roleName = JWT.require(algorithm)
                    .withIssuer("demo-adv-api")
                    .build()
                    .verify(token)
                    .getClaim("role")
                    .asString();
            return roleName != null ? UserRole.valueOf(roleName) : null;
        } catch (JWTVerificationException | IllegalArgumentException exception) {
            return null;
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
