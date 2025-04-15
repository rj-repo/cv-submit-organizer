package org.rj.auth_service.domain.user.ports.in;

import org.rj.auth_service.domain.user.dto.RegisterUserRequest;

public interface RegisterUserUseCase {

    void signup(RegisterUserRequest input);
}
