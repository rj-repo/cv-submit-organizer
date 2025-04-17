package org.rj.auth_service.infrastructure.verification.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rj.auth_service.application.verification.exception.VerificationTokenSendingException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JavaMailProviderAdapterTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private JavaMailProviderAdapter mailService;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(mailService, "subjectText", "Verification Email");
        ReflectionTestUtils.setField(mailService, "contentText", "Please verify your account by clicking the link: ");
        ReflectionTestUtils.setField(mailService, "hostProfile", "http://localhost:8080");
        ReflectionTestUtils.setField(mailService, "contextPath", "/api");
    }

    @Test
    void shouldSendMailSuccessfully() {
        // Given
        String toEmail = "test@example.com";
        String verificationCode = "123456";
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(toEmail);
        expectedMessage.setSubject("Verification Email");
        expectedMessage.setText("Please verify your account by clicking the link: http://localhost:8080/api/auth/verification?token=123456");

        // When
        mailService.sendMail(toEmail, verificationCode);

        // Then
        verify(mailSender, times(1)).send(expectedMessage);
    }

    @Test
    void shouldThrowExceptionWhenMailFailsToSend() {
        // Given
        String toEmail = "test@example.com";
        String verificationCode = "123456";
        doThrow(new MailException("Mail sending failed") {}).when(mailSender).send(any(SimpleMailMessage.class));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(
                VerificationTokenSendingException.class,
                () -> mailService.sendMail(toEmail, verificationCode)
        );
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
  
}