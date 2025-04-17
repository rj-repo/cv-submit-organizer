package org.rj.auth_service.application.user.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rj.auth_service.domain.verification.ports.out.AuthTokenProviderPort;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ValidateUserTokenServiceTest {

    @Mock
    private AuthTokenProviderPort authTokenProviderPort;

    @InjectMocks
    private ValidateUserTokenService authTokenValidator;

    @Test
    void shouldValidateTokenAfterRemovingBearerPrefix() {
        // Given
        String tokenWithBearer = "Bearer abcdefghijklmnopqrstuvwxyz";

        // When
        authTokenValidator.validate(tokenWithBearer);

        // Then
        verify(authTokenProviderPort, times(1)).validate("abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    void shouldValidateTokenWithoutBearerPrefixDirectly() {
        // Given
        String tokenWithoutBearer = "abcdefghijklmnopqrstuvwxyz";

        // When
        authTokenValidator.validate(tokenWithoutBearer);

        // Then
        verify(authTokenProviderPort, times(1)).validate(tokenWithoutBearer);
    }

}