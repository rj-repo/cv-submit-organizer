package org.rj.auth_service.it;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rj.auth_service.TestContainersInitConfiguration;
import org.rj.auth_service.domain.user.dto.LoginResponse;
import org.rj.auth_service.domain.user.dto.LoginUserRequest;
import org.rj.auth_service.domain.user.dto.RegisterUserRequest;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.verification.model.VerificationToken;
import org.rj.auth_service.infrastructure.user.persistence.JpaAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest extends TestContainersInitConfiguration {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JpaAuthUser jpaAuthUser;

    @Autowired
    private JpaVerificationTokenStub jpaVerificationTokenStub;


    @DynamicPropertySource
    static void initEnv(DynamicPropertyRegistry registry) {
        registry.add("application.security.jwt.secret-key", () -> 123);
        registry.add("application.security.jwt.expiration", () -> 10);
    }

    @BeforeEach
    public void clearDatabase() {
        jpaAuthUser.deleteAll();
    }


    @Test
    void registerUserWithExistingUsername() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "username@com.pl",
                "passworD123@"

        );

        ResponseEntity<Void> getResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(getResponse.hasBody());

        ResponseEntity<Void> getResponse1 = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);
        assertThat(getResponse1.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertFalse(getResponse1.hasBody());

    }

    @Test
    void registerValidNewUser() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "username@com.pl",
                "passworD123@"

        );

        ResponseEntity<Void> getResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(getResponse.hasBody());

    }


    @Test
    void loginUser_accountEnabled() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "username@gmail.com",
                "passworD123@"
        );

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);

        String tokenVer = getVerificationToken(registerUserRequest.email());

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/verification?token=" + tokenVer,
                getVerificationToken(registerUserRequest.email()),
                Void.class);

        LoginUserRequest loginUserRequest = new LoginUserRequest(
                "username@gmail.com",
                "passworD123@"
        );

        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/login",
                loginUserRequest,
                LoginResponse.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(loginResponse.hasBody());
        assertNotNull(loginResponse.getBody().token());
        String token = loginResponse.getBody().token();
        assertThat(decodeToken(token).getSubject()).isEqualTo("username@gmail.com");

    }

    @Test
    void loginUser_accountDisabled() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "username@gmail.com",
                "passworD123@"
        );

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);

        LoginUserRequest loginUserRequest = new LoginUserRequest(
                "username@gmail.com",
                "passworD123@"
        );

        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/login",
                loginUserRequest,
                LoginResponse.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void loginUser_badToken() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "username@com.pl",
                "passworD123@"

        );

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);

        String tokenVer = getVerificationToken(registerUserRequest.email());
        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/verification?token=" + tokenVer,
                getVerificationToken(registerUserRequest.email()),
                Void.class);
        LoginUserRequest loginUserRequest = new LoginUserRequest(
                "username@com.pl",
                "passworD123@"
        );
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/login",
                loginUserRequest,
                LoginResponse.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(loginResponse.hasBody());
        assertNotNull(loginResponse.getBody().token());
        String token = loginResponse.getBody().token();
        assertThat(decodeToken(token).getSubject()).isEqualTo("username@com.pl");

    }

    @Test
    void enableUser_resendToken() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "username@com.pl",
                "passworD123@"

        );

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);

        String tokenVer = getVerificationToken(registerUserRequest.email());
        ResponseEntity<Void> voidResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/verification/resend?token=" + tokenVer,
                getVerificationToken(registerUserRequest.email()),
                Void.class);

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(voidResponseEntity.hasBody());

    }

    @Test
    void validateToken_validToken() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "username@gmail.com",
                "passworD123@"
        );

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/signup",
                registerUserRequest,
                Void.class);

        String tokenVer = getVerificationToken(registerUserRequest.email());

        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/verification?token=" + tokenVer,
                getVerificationToken(registerUserRequest.email()),
                Void.class);

        LoginUserRequest loginUserRequest = new LoginUserRequest(
                "username@gmail.com",
                "passworD123@"
        );

        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/login",
                loginUserRequest,
                LoginResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+loginResponse.getBody().token());
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> validatingTokenResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/validation",
                requestEntity,
                Void.class);

        assertThat(validatingTokenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

    private String getVerificationToken(String email) {
        AuthUser byEmail = jpaAuthUser.findByEmail(email).get().toAggregate();
        VerificationToken byAuthUserId = jpaVerificationTokenStub.findByAuthUser_Id(byEmail.getId().id()).get().toAggregate();
        return byAuthUserId.getVerificationToken();
    }


}
