package com.root.pattern.adapter.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.domain.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtHandler {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final static Long EXP_TIME = 25200L; // 7h in seconds

    @Value("${jwt.issuer}")
    private String issuer;

    public String generate(String subject, Role role) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);
            String token = JWT.create()
                    .withIssuer(this.issuer)
                    .withExpiresAt(this.tokenExpirationTime())
                    .withSubject(subject)
                    .withClaim("app-role", role.toString())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            throw new BadRequestException("Error while creation the token...");
        }
    }

    public DecodedJWT verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.issuer)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new BadRequestException("Error while verifying the token...");
        }
    }

    private Instant tokenExpirationTime() {
        return Instant.now().plusSeconds(EXP_TIME);
    }
}
