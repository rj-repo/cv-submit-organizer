package org.rj.auth_service.domain.user.ports.in;

public interface ValidateUserTokenUseCase {

    void validate(String token);
}
