package org.rj.auth_service.infrastructure.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.application.user.exception.InvalidJwtTokenException;
import org.rj.auth_service.domain.user.model.Token;
import org.rj.auth_service.domain.verification.ports.out.AuthTokenProviderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenProviderAdapter implements AuthTokenProviderPort {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.issuer}")
    private String jwtIssuer;

    @Override
    public void validate(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withIssuer(jwtIssuer)
                .build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new InvalidJwtTokenException("Invalid token");
        }
    }

    @Override
    public Token createToken(String username) {
        String token = JWT.create()
                .withSubject(username)
                .withIssuer(jwtIssuer)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
                .sign(Algorithm.HMAC256(secretKey));

        return new Token(token);
    }
}

