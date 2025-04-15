package org.rj.auth_service.domain.user.ports.in;

import org.rj.auth_service.domain.user.dto.LoginUserRequest;

public interface AuthManagerPort {

    void authenticate(LoginUserRequest loginUserRequest);
}
