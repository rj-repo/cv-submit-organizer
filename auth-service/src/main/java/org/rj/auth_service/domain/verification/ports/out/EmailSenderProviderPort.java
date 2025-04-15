package org.rj.auth_service.domain.verification.ports.out;

public interface EmailSenderProviderPort {

    void sendMail(String mail, String verToken);
}
