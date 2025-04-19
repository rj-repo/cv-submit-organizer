package org.rj.auth_service.domain.verification.ports.out;

import org.rj.auth_service.domain.user.model.AuthUserId;
import org.rj.auth_service.domain.user.model.Token;
import org.rj.auth_service.domain.user.model.UserDetails;

public interface AuthTokenProviderPort {

    Token createToken(AuthUserId userId,String email);
    UserDetails validate(String token);

}
