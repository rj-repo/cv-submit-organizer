package org.rj.auth_service.domain.user.ports.out;

public interface PasswordEncoderPort {

    String encode(String password);
}
