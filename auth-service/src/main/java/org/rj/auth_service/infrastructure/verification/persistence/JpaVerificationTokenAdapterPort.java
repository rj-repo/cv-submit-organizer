package org.rj.auth_service.infrastructure.verification.persistence;

import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.verification.model.VerificationToken;
import org.rj.auth_service.domain.verification.ports.out.VerificationTokenRepoPort;
import org.rj.auth_service.infrastructure.verification.persistence.entity.VerificationTokenEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaVerificationTokenAdapterPort implements VerificationTokenRepoPort {

    private final JpaVerificationToken verificationTokenRepository;


    @Override
    public void save(VerificationToken verificationToken) {
        verificationTokenRepository.save(VerificationTokenEntity.toEntity(verificationToken));
    }

    @Override
    public Optional<VerificationToken> findByVerificationToken(String token) {
        return verificationTokenRepository.findByVerificationToken(token).map(VerificationTokenEntity::toAggregate);
    }
}
