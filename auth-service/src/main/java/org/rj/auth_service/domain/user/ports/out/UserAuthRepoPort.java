package org.rj.auth_service.domain.user.ports.out;

import org.rj.auth_service.domain.user.model.AuthUser;

import java.util.Optional;

public interface UserAuthRepoPort {

    Optional<AuthUser> findByEmail(String email);

    AuthUser save(AuthUser authUser);

    Optional<AuthUser> findById(Long id);

}
