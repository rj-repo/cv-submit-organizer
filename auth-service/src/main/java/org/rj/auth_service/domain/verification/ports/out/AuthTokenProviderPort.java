package org.rj.auth_service.domain.verification.ports.out;

import org.rj.auth_service.domain.user.model.Token;

public interface AuthTokenProviderPort {

    Token createToken(String username);
    void validate(String token);

}
