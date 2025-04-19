package org.rj.auth_service.application.rest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.user.dto.LoginResponse;
import org.rj.auth_service.domain.user.dto.LoginUserRequest;
import org.rj.auth_service.domain.user.dto.RegisterUserRequest;
import org.rj.auth_service.domain.user.dto.UserModifyRequest;
import org.rj.auth_service.domain.user.model.UserDetails;
import org.rj.auth_service.domain.user.ports.in.EnableUserUseCase;
import org.rj.auth_service.domain.user.ports.in.LoginUserUseCase;
import org.rj.auth_service.domain.user.ports.in.RegisterUserUseCase;
import org.rj.auth_service.domain.user.ports.in.ValidateUserTokenUseCase;
import org.rj.auth_service.domain.verification.dto.VerificationTokenRequest;
import org.rj.auth_service.domain.verification.ports.out.ResendVerificationTokenUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class UserAuthResource {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final EnableUserUseCase enableUserUseCase;
    private final ResendVerificationTokenUseCase resendVerificationTokenUseCase;
    private final ValidateUserTokenUseCase validateUserTokenUseCase;


    @PostMapping("/registration")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequest registerUserDto) {
        registerUserUseCase.signup(registerUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification")
    public ResponseEntity<Void> verify(@RequestParam VerificationTokenRequest token) {
        enableUserUseCase.enableUser(token.token());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification/resend")
    public ResponseEntity<Void> resendVerificationToken(@RequestParam VerificationTokenRequest token) {
        resendVerificationTokenUseCase.resendVerificationToken(token.token());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserRequest tokenRequestDto) {
        LoginResponse authenticatedUser = loginUserUseCase.login(tokenRequestDto);
        return ResponseEntity.ok(authenticatedUser);
    }


    @PostMapping("/validation")
    public ResponseEntity<Void> validation(@RequestHeader("Authorization") String token) {
        UserDetails validate = validateUserTokenUseCase.validate(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Email", validate.email());
        headers.add("X-User-Id", validate.userId());

        return ResponseEntity.ok().headers(headers).build();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> modify(@RequestBody @Valid UserModifyRequest userModifyRequest) {
        return null;
    }


}
