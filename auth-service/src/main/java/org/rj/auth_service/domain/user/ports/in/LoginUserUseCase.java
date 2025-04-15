package org.rj.auth_service.domain.user.ports.in;

import org.rj.auth_service.domain.user.dto.LoginResponse;
import org.rj.auth_service.domain.user.dto.LoginUserRequest;

public interface LoginUserUseCase {
    LoginResponse login(LoginUserRequest loginUserRequest);
}
