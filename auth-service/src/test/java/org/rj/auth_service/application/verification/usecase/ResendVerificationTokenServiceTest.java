package org.rj.auth_service.application.verification.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rj.auth_service.application.user.exception.AuthUserNotFoundException;
import org.rj.auth_service.application.verification.exception.VerificationTokenNotFoundException;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.model.AuthUserId;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.auth_service.domain.verification.VerificationTokenDomainService;
import org.rj.auth_service.domain.verification.model.VerificationToken;
import org.rj.auth_service.domain.verification.ports.out.EmailSenderProviderPort;
import org.rj.auth_service.domain.verification.ports.out.VerificationTokenRepoPort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResendVerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepoPort verificationTokenRepoPort;

    @Mock
    private EmailSenderProviderPort javaMailProvider;

    @Mock
    private VerificationTokenDomainService verificationTokenDomainService;

    @Mock
    private UserAuthRepoPort userAuthRepoPort;

    @InjectMocks
    private ResendVerificationTokenService resendVerificationTokenService;

    @Test
    void generateAndSendNewTokenSuccessfully() {
        // given
        String oldToken = "abc123";
        String email = "test@example.com";

        VerificationToken verificationToken = VerificationToken.builder()
                .verificationToken(oldToken)
                .userId(new AuthUserId(1L))
                .build();
        AuthUser authUser = AuthUser.builder()
                        .email(email).build();

        when(verificationTokenRepoPort.findByVerificationToken(oldToken))
                .thenReturn(Optional.of(verificationToken));

        when(userAuthRepoPort.findById(anyLong()))
                .thenReturn(Optional.of(authUser));

        // when
        resendVerificationTokenService.resendVerificationToken(oldToken);

        // then
        verify(verificationTokenRepoPort).findByVerificationToken(anyString());
        verify(userAuthRepoPort).findById(anyLong());
        verify(verificationTokenDomainService).generateNewToken(any(VerificationToken.class));
        verify(verificationTokenRepoPort).save(any(VerificationToken.class));
        verify(javaMailProvider).sendMail(anyString(), anyString());
    }

    @Test
    void throwExceptionWhenTokenNotFound() {
        // given
        String token = "invalid-token";
        when(verificationTokenRepoPort.findByVerificationToken(token)).thenReturn(Optional.empty());

        // then
        assertThrows(VerificationTokenNotFoundException.class, () -> {
            resendVerificationTokenService.resendVerificationToken(token);
        });

        verifyNoInteractions(userAuthRepoPort, verificationTokenDomainService, javaMailProvider);
    }

    @Test
    void throwExceptionWhenResendTokenUserNotFound() {
        // given
        String token = "valid-token";
        VerificationToken verificationToken = VerificationToken.builder()
                .verificationToken(token)
                .userId(new AuthUserId(1L))
                .build();


        when(verificationTokenRepoPort.findByVerificationToken(anyString())).thenReturn(Optional.of(verificationToken));
        when(userAuthRepoPort.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(AuthUserNotFoundException.class, () -> {
            resendVerificationTokenService.resendVerificationToken(token);
        });

        verify(verificationTokenRepoPort).findByVerificationToken(anyString());
        verify(userAuthRepoPort).findById(anyLong());
        verifyNoMoreInteractions(verificationTokenDomainService, javaMailProvider);
    }
}