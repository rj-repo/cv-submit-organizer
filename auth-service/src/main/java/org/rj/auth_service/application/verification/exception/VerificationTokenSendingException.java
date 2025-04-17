package org.rj.auth_service.application.verification.exception;

import org.rj.cvsubmitorganizer.common.ResponseException;
import org.springframework.http.HttpStatus;

public class VerificationTokenSendingException extends ResponseException {

    private final String message;

    public VerificationTokenSendingException(String message) {
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return message;
    }

   
}
