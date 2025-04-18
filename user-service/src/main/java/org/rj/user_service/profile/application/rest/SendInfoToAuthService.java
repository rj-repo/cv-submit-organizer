package org.rj.user_service.profile.application.rest;

import lombok.RequiredArgsConstructor;
import org.rj.user_service.profile.application.exception.RegisterNewUserExcpetion;
import org.rj.user_service.profile.application.rest.dto.RegisterAuthRequest;
import org.rj.user_service.profile.domain.model.SaveNewUserRequest;
import org.rj.user_service.profile.infrastructure.ExternalErrorExcpetion;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SendInfoToAuthService {

    private final RestTemplate restTemplate;

    public void send(SaveNewUserRequest saveNewUserRequest) {
        RegisterAuthRequest request = new RegisterAuthRequest(saveNewUserRequest.email(), saveNewUserRequest.password());
        try {
            restTemplate.postForEntity("http://auth-service/api/v1/auth/signup", request, Void.class);
        }catch (ExternalErrorExcpetion e){
            throw new RegisterNewUserExcpetion(e.getApiResponse().message());
        }
    }
}
