package org.rj.auth_service.infrastructure.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.application.user.exception.InvalidJwtTokenException;
import org.rj.auth_service.domain.user.model.AuthUserId;
import org.rj.auth_service.domain.user.model.Token;
import org.rj.auth_service.domain.user.model.UserDetails;
import org.rj.auth_service.domain.verification.ports.out.AuthTokenProviderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public UserDetails validate(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withIssuer(jwtIssuer)
                .build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            return new UserDetails(verify.getClaim("email").asString(), verify.getSubject());
        } catch (JWTVerificationException e) {
            throw new InvalidJwtTokenException("Invalid token");
        }
    }

    @Override
    public Token createToken(AuthUserId userId, String email) {
        String token = JWT.create()
                .withSubject(String.valueOf(userId.id()))
                .withClaim("email", email)
                .withIssuer(jwtIssuer)
                .withIssuedAt(new Date(Instant.now().toEpochMilli()))
                .withExpiresAt(new Date(Instant.now().plusSeconds(jwtExpiration).toEpochMilli()))
                .sign(Algorithm.HMAC256(secretKey));

        return new Token(token);
    }
}

