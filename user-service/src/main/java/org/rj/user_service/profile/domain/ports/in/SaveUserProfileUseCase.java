package org.rj.user_service.profile.domain.ports.in;

import org.rj.user_service.profile.domain.model.SaveNewUserRequest;

public interface SaveUserProfileUseCase {

    void save(SaveNewUserRequest userProfile);
}
