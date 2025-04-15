package org.rj.auth_service.infrastructure.common.rest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.user.dto.LoginResponse;
import org.rj.auth_service.domain.user.dto.LoginUserRequest;
import org.rj.auth_service.domain.user.dto.RegisterUserRequest;
import org.rj.auth_service.domain.user.ports.in.EnableUserUseCase;
import org.rj.auth_service.domain.user.ports.in.LoginUserUseCase;
import org.rj.auth_service.domain.user.ports.in.RegisterUserUseCase;
import org.rj.auth_service.domain.user.ports.in.ValidateUserTokenUseCase;
import org.rj.auth_service.domain.verification.ports.out.ResendVerificationTokenUseCase;
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


    @PostMapping("/signup")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequest registerUserDto) {
        registerUserUseCase.signup(registerUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification")
    public ResponseEntity<Void> verify(@RequestParam String token) {
        enableUserUseCase.enableUser(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification/resend")
    public ResponseEntity<Void> resendVerificationToken(@RequestParam String token) {
        resendVerificationTokenUseCase.resendVerificationToken(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserRequest tokenRequestDto) {
        LoginResponse authenticatedUser = loginUserUseCase.login(tokenRequestDto);
        return ResponseEntity.ok(authenticatedUser);
    }


    @GetMapping("/validation")
    public ResponseEntity<Void> loginAndRetrieveToken(@RequestHeader("Authorization") String token) {
        validateUserTokenUseCase.validate(token);
        return ResponseEntity.ok().build();
    }


}
