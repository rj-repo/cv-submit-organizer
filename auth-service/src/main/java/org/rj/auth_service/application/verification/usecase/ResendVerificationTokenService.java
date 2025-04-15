package org.rj.auth_service.application.verification.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.application.user.exception.AuthUserNotFoundException;
import org.rj.auth_service.application.verification.exception.VerificationTokenNotFoundException;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.auth_service.domain.verification.VerificationTokenDomainService;
import org.rj.auth_service.domain.verification.model.VerificationToken;
import org.rj.auth_service.domain.verification.ports.out.EmailSenderProviderPort;
import org.rj.auth_service.domain.verification.ports.out.ResendVerificationTokenUseCase;
import org.rj.auth_service.domain.verification.ports.out.VerificationTokenRepoPort;
import org.rj.cvsubmitorganizer.common.UseCaseService;

@UseCaseService
@RequiredArgsConstructor
public class ResendVerificationTokenService implements ResendVerificationTokenUseCase {

    private final VerificationTokenRepoPort verificationTokenRepoPort;
    private final EmailSenderProviderPort javaMailProvider;
    private final VerificationTokenDomainService verificationTokenDomainService;
    private final UserAuthRepoPort userAuthRepoPort;


    @Override
    @Transactional
    public void resendVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepoPort.findByVerificationToken(token)
                .orElseThrow(() -> new VerificationTokenNotFoundException("Verification token not found"));
        AuthUser userVerificaitonToken = userAuthRepoPort.findById(verificationToken.getUserId().id())
                .orElseThrow(() -> new AuthUserNotFoundException("User not found"));
        verificationTokenDomainService.generateNewToken(verificationToken);
        verificationTokenRepoPort.save(verificationToken);
        javaMailProvider.sendMail(userVerificaitonToken.getEmail(), verificationToken.getVerificationToken());
    }
}
