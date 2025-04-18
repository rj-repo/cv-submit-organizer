package org.rj.user_service.profile.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SaveNewUserRequest(@NotBlank(message = "Email cannot be blank") String email,
                                 @NotBlank(message = "firstname cannot be blank") String firstname,
                                 @NotBlank(message = "password cannot be blank") String password,
                                 @NotBlank(message = "surname cannot be blank") String surname) {

    public UserProfile toAggregate() {
        return UserProfile.builder()
                .email(email)
                .firstname(firstname)
                .surname(surname)
                .build();
    }
}
