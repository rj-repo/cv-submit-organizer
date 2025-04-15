package org.rj.auth_service.application.verification.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.user.model.AuthUserId;
import org.rj.auth_service.domain.verification.VerificationTokenDomainService;
import org.rj.auth_service.domain.verification.model.VerificationToken;
import org.rj.auth_service.domain.verification.ports.out.EmailSenderProviderPort;
import org.rj.auth_service.domain.verification.ports.out.SendVerificationTokenPort;
import org.rj.auth_service.domain.verification.ports.out.VerificationTokenRepoPort;
import org.rj.cvsubmitorganizer.common.UseCaseService;

@UseCaseService
@RequiredArgsConstructor
public class SendVerificationTokenService implements SendVerificationTokenPort {

    private final VerificationTokenRepoPort verificationTokenRepoPort;
    private final EmailSenderProviderPort javaMailProvider;
    private final VerificationTokenDomainService verificationTokenDomainService;

    @Override
    @Transactional
    public void sendToken(AuthUserId userId, String userEmail) {
        VerificationToken tokeVer;
        do {
            tokeVer = verificationTokenDomainService.createVerificationToken(userId);
        } while (verificationTokenRepoPort.findByVerificationToken(tokeVer.getVerificationToken()).isPresent());
        verificationTokenRepoPort.save(tokeVer);
        javaMailProvider.sendMail(userEmail, tokeVer.getVerificationToken());
    }

}
