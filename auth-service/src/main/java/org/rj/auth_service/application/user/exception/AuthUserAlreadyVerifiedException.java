package org.rj.auth_service.application.user.exception;

import org.rj.cvsubmitorganizer.common.ResponseException;
import org.springframework.http.HttpStatus;

public class AuthUserAlreadyVerifiedException extends ResponseException {

    private final String message;

    public AuthUserAlreadyVerifiedException(String message) {
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
