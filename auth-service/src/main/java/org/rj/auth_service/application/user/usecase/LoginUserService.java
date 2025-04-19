package org.rj.auth_service.application.user.usecase;

import lombok.RequiredArgsConstructor;
import org.rj.auth_service.application.user.exception.AuthUserNotFoundException;
import org.rj.auth_service.domain.user.dto.LoginResponse;
import org.rj.auth_service.domain.user.dto.LoginUserRequest;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.model.Token;
import org.rj.auth_service.domain.user.ports.in.AuthManagerPort;
import org.rj.auth_service.domain.user.ports.in.LoginUserUseCase;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.auth_service.domain.verification.ports.out.AuthTokenProviderPort;
import org.rj.cvsubmitorganizer.common.UseCaseService;

@UseCaseService
@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {
    private final AuthManagerPort authManagerPort;
    private final AuthTokenProviderPort tokenService;
    private final UserAuthRepoPort userRepository;

    @Override
    public LoginResponse login(LoginUserRequest loginUserRequest) {
        AuthUser authUser = userRepository.findByEmail(loginUserRequest.email())
                .orElseThrow(() -> new AuthUserNotFoundException("User " + loginUserRequest.email() + " not found"));
        authUser.checkIfUserEnabled();
        authManagerPort.authenticate(loginUserRequest);
        Token token = tokenService.createToken(authUser.getId(),authUser.getEmail());

        return new LoginResponse(token.token());
    }

}
