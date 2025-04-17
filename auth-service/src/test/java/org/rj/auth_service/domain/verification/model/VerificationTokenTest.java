package org.rj.auth_service.domain.verification.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class VerificationTokenTest {

    @Test
    void shouldSetExpirationDate10MinutesFromNow() {
        //given
        VerificationToken verificationToken = VerificationToken.builder().build();

        //when
        verificationToken.setExpirationDate(10);

        // then
        LocalDateTime now = LocalDateTime.now();
        assertNotNull(verificationToken.getExpirationDate());
        assertTrue(verificationToken.getExpirationDate().isAfter(now));
        assertTrue(verificationToken.getExpirationDate().isBefore(now.plusMinutes(11)));
    }

    @Test
    void shouldGenerateVerificationToken() {
        //given
        VerificationToken verificationToken = VerificationToken.builder().build();

        // when
        verificationToken.generateToken();

        // then
        assertNotNull(verificationToken.getVerificationToken());
        assertDoesNotThrow(() -> UUID.fromString(verificationToken.getVerificationToken()));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsExpired() {
        //given
        VerificationToken verificationToken = VerificationToken.builder().build();
        LocalDateTime fixedDateTime = LocalDateTime.of(2030, 4, 17, 10, 0);
        verificationToken.setExpirationDate(10);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(fixedDateTime);

            // when
            VerificationTokenDomainException exception = assertThrows(
                    VerificationTokenDomainException.class,
                    verificationToken::checkTokenExpiration
            );
            // then
            assertEquals("Token expired", exception.getMessage());
        }
    }

    @Test
    void shouldNotThrowExceptionWhenTokenIsValid() {
        //given
        VerificationToken verificationToken = VerificationToken.builder().build();
        // when
        verificationToken.setExpirationDate(10); 

        // then
        assertDoesNotThrow(verificationToken::checkTokenExpiration);
    }
}