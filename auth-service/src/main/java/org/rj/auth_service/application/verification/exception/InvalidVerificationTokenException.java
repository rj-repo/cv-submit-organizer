package org.rj.auth_service.application.verification.exception;

import org.rj.cvsubmitorganizer.common.ResponseException;
import org.springframework.http.HttpStatus;

public class InvalidVerificationTokenException extends ResponseException {

    private final String message;

    public InvalidVerificationTokenException(String message) {
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getClassName() {
        return getClass().getName();
    }
}
