package org.rj.auth_service.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import org.rj.auth_service.domain.user.model.AuthUser;

public record RegisterUserRequest(@NotBlank(message = "Email cannot be blank") String email,
                                  @NotBlank(message = "Password cannot be blank") String password) {

    public AuthUser toAggregate() {
        return AuthUser.builder()
                .password(password)
                .email(email)
                .build();
    }

}
