package org.rj.auth_service.domain.verification;

import org.rj.auth_service.domain.user.model.AuthUserId;
import org.rj.auth_service.domain.verification.model.VerificationToken;

public class VerificationTokenDomainService {

    public VerificationToken createVerificationToken(AuthUserId userId) {
        VerificationToken verificationToken = VerificationToken.builder()
                .userId(userId)
                .build();
        verificationToken.setExpirationDate(10);
        verificationToken.generateToken();
        return verificationToken;
    }

    public void generateNewToken(VerificationToken oldToken) {
        oldToken.generateToken();
        oldToken.setExpirationDate(10);
    }
}
