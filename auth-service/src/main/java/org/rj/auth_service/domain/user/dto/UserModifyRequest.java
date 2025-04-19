package org.rj.auth_service.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserModifyRequest(@NotBlank(message = "Email cannot be blank") String email) {
}
