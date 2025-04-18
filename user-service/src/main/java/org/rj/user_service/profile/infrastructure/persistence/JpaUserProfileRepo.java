package org.rj.user_service.profile.infrastructure.persistence;

import org.rj.user_service.profile.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserProfileRepo extends JpaRepository<UserProfileEntity, Long> {

    Optional<UserProfileEntity> findByEmail(String email);
}
