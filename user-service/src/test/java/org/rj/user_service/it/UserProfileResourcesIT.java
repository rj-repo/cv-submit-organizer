package org.rj.user_service.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rj.cvsubmitorganizer.common.ApiResponseException;
import org.rj.user_service.profile.application.exception.RegisterNewUserExcpetion;
import org.rj.user_service.profile.application.rest.SendInfoToAuthService;
import org.rj.user_service.profile.domain.model.SaveNewUserRequest;
import org.rj.user_service.profile.infrastructure.persistence.JpaUserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class UserProfileResourcesIT extends TestContainersInitConfiguration {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JpaUserProfileRepo jpaUserProfileRepo;

    @MockitoBean
    private SendInfoToAuthService sendInfoToAuthService;

    @AfterEach
    public void cleanDb(){
        jpaUserProfileRepo.deleteAll();
    }

    @Test
    public void registration_success() {

        SaveNewUserRequest saveNewUserRequest = SaveNewUserRequest
                .builder()
                .email("username@com.pl")
                .password("123Password@")
                .firstname("firstname")
                .surname("surname")
                .build();

        doNothing().when(sendInfoToAuthService).send(saveNewUserRequest);

        ResponseEntity<Void> voidResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/profile/registration",
                saveNewUserRequest,
                Void.class);

        assertThat(voidResponseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void registration_throwExcpetionFromExternalService() {

        SaveNewUserRequest saveNewUserRequest = SaveNewUserRequest
                .builder()
                .email("username@com.pl")
                .password("123Password@")
                .firstname("firstname")
                .surname("surname")
                .build();


        doThrow(new RegisterNewUserExcpetion("Houston - we have a problem")).when(sendInfoToAuthService).send(any(SaveNewUserRequest.class));

        ResponseEntity<ApiResponseException> voidResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/profile/registration",
                saveNewUserRequest,
                ApiResponseException.class);

        assertThat(voidResponseEntity.getStatusCode().value()).isEqualTo(400);
        assertTrue(voidResponseEntity.hasBody());
        assertThat(voidResponseEntity.getBody().message()).isEqualTo("Houston - we have a problem");
    }
}
