package org.rj.user_service.profile.domain.ports.out;

import org.rj.user_service.profile.domain.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepoPort {

    void save(UserProfile userProfile);
    Optional<UserProfile> findByEmail(String email);
}
