package org.rj.auth_service.infrastructure.verification.persistence;

import org.rj.auth_service.infrastructure.verification.persistence.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaVerificationToken extends JpaRepository<VerificationTokenEntity, Long> {

    Optional<VerificationTokenEntity> findByVerificationToken(String verificationToken);
}
