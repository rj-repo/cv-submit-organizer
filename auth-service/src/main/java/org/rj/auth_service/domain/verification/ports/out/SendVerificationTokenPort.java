package org.rj.auth_service.domain.verification.ports.out;

import org.rj.auth_service.domain.user.model.AuthUserId;

public interface SendVerificationTokenPort {

    void sendToken(AuthUserId userId, String userEmail);

}
