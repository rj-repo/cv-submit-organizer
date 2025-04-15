package org.rj.auth_service.it;

import org.rj.auth_service.infrastructure.verification.persistence.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaVerificationTokenStub extends JpaRepository<VerificationTokenEntity, Long> {
    Optional<VerificationTokenEntity> findByAuthUser_Id(Long authUserId);
}
