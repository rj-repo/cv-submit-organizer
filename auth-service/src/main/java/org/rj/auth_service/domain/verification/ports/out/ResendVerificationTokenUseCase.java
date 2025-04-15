package org.rj.auth_service.domain.verification.ports.out;

public interface ResendVerificationTokenUseCase {

    void resendVerificationToken(String token);
}
