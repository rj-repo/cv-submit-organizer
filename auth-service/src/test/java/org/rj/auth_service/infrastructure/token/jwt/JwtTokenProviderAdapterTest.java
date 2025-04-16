package org.rj.auth_service.infrastructure.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rj.auth_service.application.user.exception.InvalidJwtTokenException;
import org.rj.auth_service.domain.user.model.Token;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderAdapterTest {

    @InjectMocks
    private JwtTokenProviderAdapter jwtService;

    @BeforeEach
    public void setUp(){

        ReflectionTestUtils.setField(jwtService, "secretKey", "secretKey");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000);
        ReflectionTestUtils.setField(jwtService, "jwtIssuer", "test_issuer");
    }

    @Test
    void createTokenAndReturnValidUsername() {
        //given
        String username = "mockuser";

        //when
        Token token = jwtService.createToken(username);

        //then
        assertNotNull(token);
        assertEquals(username,decodeToken(token.token()).getSubject());
        assertNotNull(token.token());
    }

    @Test
    void validateTokenSuccessfully() {
        //given
        String username = "validuser";
        String bearerToken = jwtService.createToken(username).token();

        //then-throw
        assertDoesNotThrow(() -> jwtService.validate(bearerToken));
    }

    @Test
    void throwsExceptionDueToInvalidJwtToken() {
        //given
        String badToken = "invalid.token.value";

        //then-throw
        InvalidJwtTokenException exception = assertThrows(
                InvalidJwtTokenException.class,
                () -> jwtService.validate(badToken)
        );

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void throwsExceptionDueToInvalidSecret() {
        //given
        String username = "username";
        Token generatedToken = jwtService.createToken(username);

        ReflectionTestUtils.setField(jwtService, "secretKey", "newsecretKey");


        //then-throw
        InvalidJwtTokenException exception = assertThrows(
                InvalidJwtTokenException.class,
                () -> jwtService.validate(generatedToken.token())
        );

        assertEquals("Invalid token", exception.getMessage());
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

}