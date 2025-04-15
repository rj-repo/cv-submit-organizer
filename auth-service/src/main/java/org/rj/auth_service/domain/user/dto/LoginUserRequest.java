package org.rj.auth_service.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(@NotBlank(message = "Username cannot be blank") String email,
                               @NotBlank(message = "Password cannot be blank") String password) {
}
