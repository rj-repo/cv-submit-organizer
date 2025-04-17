package org.rj.auth_service.infrastructure.verification.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rj.auth_service.application.verification.exception.VerificationTokenSendingException;
import org.rj.auth_service.domain.verification.ports.out.EmailSenderProviderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JavaMailProviderAdapter implements EmailSenderProviderPort {

    private final JavaMailSender mailSender;

    @Value("${auth.mail.subject}")
    private String subjectText;

    @Value("${auth.mail.content-text}")
    private String contentText;

    @Value("${application.host.url}")
    private String hostProfile;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void sendMail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        String urlToRedirect = String.format("%s%s/auth/verification?token=%s", hostProfile, contextPath, verificationCode);
        message.setTo(toEmail);
        message.setSubject(subjectText);
        message.setText(contentText + urlToRedirect);
        try {
            mailSender.send(message);
        } catch (MailException e){
            log.error("Error sending mail", e);
            throw new VerificationTokenSendingException("Token cannot be sent - internal error occured");
        }
    }
}
