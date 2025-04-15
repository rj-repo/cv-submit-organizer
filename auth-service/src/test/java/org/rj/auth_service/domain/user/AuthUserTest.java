package org.rj.auth_service.domain.user;

import org.junit.Test;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.model.AuthUserDomainException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AuthUserTest {

    @Test
    public void validateEmail_shouldThrow_whenEmailIsInvalid() {
        AuthUser user = AuthUser.builder()
                .email("invalid-email")
                .password("Str0ng@Pass")
                .enabled(true)
                .build();

        assertThatThrownBy(user::validateEmail)
                .isInstanceOf(AuthUserDomainException.class)
                .hasMessage("Wrong email");
    }

    @Test
    public void validateEmail_shouldPass_whenEmailIsValid() {
        AuthUser user = AuthUser.builder()
                .email("user@example.com")
                .password("Str0ng@Pass")
                .enabled(true)
                .build();

        assertThatNoException().isThrownBy(user::validateEmail);
    }

    @Test
    public void validatePassword_shouldThrow_whenPasswordIsWeak() {
        AuthUser user = AuthUser.builder()
                .email("user@example.com")
                .password("weak")
                .enabled(true)
                .build();

        assertThatThrownBy(user::validatePassword)
                .isInstanceOf(AuthUserDomainException.class)
                .hasMessage("Wrong password");
    }

    @Test
    public void validatePassword_shouldPass_whenPasswordIsStrong() {
        AuthUser user = AuthUser.builder()
                .email("user@example.com")
                .password("Str0ng@Pass1")
                .enabled(true)
                .build();

        assertThatNoException().isThrownBy(user::validatePassword);
    }

    @Test
    public void checkIfUserEnabled_shouldThrow_whenUserIsDisabled() {
        AuthUser user = AuthUser.builder()
                .email("user@example.com")
                .password("Str0ng@Pass")
                .enabled(false)
                .build();

        assertThatThrownBy(user::checkIfUserEnabled)
                .isInstanceOf(AuthUserDomainException.class)
                .hasMessage("User is not enabled");
    }

    @Test
    public void checkIfUserEnabled_shouldPass_whenUserIsEnabled() {
        AuthUser user = AuthUser.builder()
                .email("user@example.com")
                .password("Str0ng@Pass")
                .enabled(true)
                .build();

        assertThatNoException().isThrownBy(user::checkIfUserEnabled);
    }
}