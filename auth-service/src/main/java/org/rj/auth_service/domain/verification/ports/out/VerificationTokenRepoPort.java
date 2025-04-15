package org.rj.auth_service.domain.verification.ports.out;

import org.rj.auth_service.domain.verification.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepoPort {

    void save(VerificationToken verificationToken);

    Optional<VerificationToken> findByVerificationToken(String token);
}
