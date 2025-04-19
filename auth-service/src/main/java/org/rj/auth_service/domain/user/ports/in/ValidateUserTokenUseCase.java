package org.rj.auth_service.domain.user.ports.in;

import org.rj.auth_service.domain.user.model.UserDetails;

public interface ValidateUserTokenUseCase {

    UserDetails validate(String token);
}
