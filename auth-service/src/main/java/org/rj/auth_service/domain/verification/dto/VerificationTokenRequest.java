package org.rj.auth_service.domain.verification.dto;

import jakarta.validation.constraints.NotBlank;

public record VerificationTokenRequest(@NotBlank(message = "Token is blank") String token) {


}
