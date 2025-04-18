package org.rj.user_service.profile.application.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.rj.user_service.profile.domain.model.SaveNewUserRequest;
import org.rj.user_service.profile.domain.ports.in.SaveUserProfileUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileResource {

    private final SaveUserProfileUseCase saveUserProfileUseCase;
    private final SendInfoToAuthService sendInfoToAuthService;


    @PostMapping(path = "/registration")
    public ResponseEntity<Void> save(@RequestBody @Valid SaveNewUserRequest saveNewUserRequest) {
        sendInfoToAuthService.send(saveNewUserRequest);
        saveUserProfileUseCase.save(saveNewUserRequest);
        return ResponseEntity.ok().build();
    }

}
