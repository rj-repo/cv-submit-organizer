package org.rj.auth_service.infrastructure.user.persistence;

import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.auth_service.infrastructure.user.persistence.entity.AuthUserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAuthUserAdapterPort implements UserAuthRepoPort {

    private final JpaAuthUser jpaAuthUser;

    @Override
    public Optional<AuthUser> findByEmail(String username) {
        return jpaAuthUser.findByEmail(username).map(AuthUserEntity::toAggregate);
    }

    @Override
    public AuthUser save(AuthUser authUser) {
        return jpaAuthUser.save(AuthUserEntity.toEntity(authUser)).toAggregate();
    }

    @Override
    public Optional<AuthUser> findById(Long id) {
        return jpaAuthUser.findById(id).map(AuthUserEntity::toAggregate);
    }
}
