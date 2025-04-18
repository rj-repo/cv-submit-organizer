package org.rj.user_service.profile.application;

import lombok.RequiredArgsConstructor;
import org.rj.cvsubmitorganizer.common.UseCaseService;
import org.rj.user_service.profile.application.exception.UserProfielAlreadyExists;
import org.rj.user_service.profile.domain.model.SaveNewUserRequest;
import org.rj.user_service.profile.domain.model.UserProfile;
import org.rj.user_service.profile.domain.ports.in.SaveUserProfileUseCase;
import org.rj.user_service.profile.domain.ports.out.UserProfileRepoPort;

import java.util.Optional;

@RequiredArgsConstructor
@UseCaseService
public class SaveUserProfileService implements SaveUserProfileUseCase {

    private final UserProfileRepoPort profileRepoPort;

    @Override
    public void save(SaveNewUserRequest userProfile) {
        Optional<UserProfile> profile = profileRepoPort.findByEmail(userProfile.email());
        if (profile.isPresent()) {
            throw new UserProfielAlreadyExists("User " + userProfile.email() + " already exists");
        }
        profileRepoPort.save(userProfile.toAggregate());
    }
}
