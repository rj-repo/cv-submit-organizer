package org.rj.auth_service.application.user.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.application.user.exception.AuthUserAlreadyVerifiedException;
import org.rj.auth_service.application.user.exception.AuthUserNotFoundException;
import org.rj.auth_service.application.verification.exception.VerificationTokenNotFoundException;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.ports.in.EnableUserUseCase;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.auth_service.domain.verification.model.VerificationToken;
import org.rj.auth_service.domain.verification.ports.out.VerificationTokenRepoPort;
import org.rj.cvsubmitorganizer.common.UseCaseService;

@UseCaseService
@RequiredArgsConstructor
public class EnableUserService implements EnableUserUseCase {

    private final UserAuthRepoPort userRepository;
    private final VerificationTokenRepoPort verificationTokenRepoPort;

    @Override
    @Transactional
    public void enableUser(String verificationTokenRequest) {
        VerificationToken verificationToken = verificationTokenRepoPort.findByVerificationToken(verificationTokenRequest)
                .orElseThrow(() -> new VerificationTokenNotFoundException("Token not found"));
        AuthUser authUser = userRepository.findById(verificationToken.getUserId().id())
                .orElseThrow(() -> new AuthUserNotFoundException("User not found"));
        if (authUser.isAccountEnabled()) {
            throw new AuthUserAlreadyVerifiedException("User already verified");
        }
        verificationToken.checkTokenExpiration();
        authUser.enableAccount();
        userRepository.save(authUser);
    }
}