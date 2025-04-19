package org.rj.auth_service.infrastructure.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rj.auth_service.application.user.exception.InvalidJwtTokenException;
import org.rj.auth_service.domain.user.model.AuthUserId;
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
        AuthUserId authUserId = new AuthUserId(1L);
        String email  = "username@gmail.com";
        //when
        Token token = jwtService.createToken(authUserId,email);

        //then
        assertNotNull(token);
        assertEquals(String.valueOf(authUserId.id()),decodeToken(token.token()).getSubject());
        assertNotNull(token.token());
    }

    @Test
    void validateTokenSuccessfully() {
        //given
        AuthUserId authUserId = new AuthUserId(1L);
        String email  = "username@gmail.com";
        //when
        Token token = jwtService.createToken(authUserId,email);

        //then-throw
        assertDoesNotThrow(() -> jwtService.validate(token.token()));
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
        //given
        AuthUserId authUserId = new AuthUserId(1L);
        String email  = "username@gmail.com";
        //when
        Token token = jwtService.createToken(authUserId,email);

        ReflectionTestUtils.setField(jwtService, "secretKey", "newsecretKey");


        //then-throw
        InvalidJwtTokenException exception = assertThrows(
                InvalidJwtTokenException.class,
                () -> jwtService.validate(token.token())
        );

        assertEquals("Invalid token", exception.getMessage());
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

}