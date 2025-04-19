package org.rj.auth_service.application.user.usecase;

import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.user.model.UserDetails;
import org.rj.auth_service.domain.user.ports.in.ValidateUserTokenUseCase;
import org.rj.auth_service.domain.verification.ports.out.AuthTokenProviderPort;
import org.rj.cvsubmitorganizer.common.UseCaseService;

@UseCaseService
@RequiredArgsConstructor
public class ValidateUserTokenService implements ValidateUserTokenUseCase {

    private final AuthTokenProviderPort authTokenProviderPort;

    @Override
    public UserDetails validate(String token) {
        if (token.contains("Bearer ")) {
            token = token.substring(7);
        }
        return authTokenProviderPort.validate(token);
    }
}
