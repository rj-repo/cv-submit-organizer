package org.rj.auth_service.domain.user;

import org.rj.auth_service.domain.user.dto.RegisterUserRequest;
import org.rj.auth_service.domain.user.model.AuthUser;

public class AuthDomainService {

    public AuthUser createUser(RegisterUserRequest registerUserRequest) {
        AuthUser aggregate = registerUserRequest.toAggregate();
        aggregate.validatePassword();
        aggregate.validateEmail();
        return aggregate;
    }
}
