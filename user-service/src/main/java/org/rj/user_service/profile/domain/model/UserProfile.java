package org.rj.user_service.profile.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProfile {
    private UserProfileId id;
    private String email;
    private String firstname;
    private String surname;
}
