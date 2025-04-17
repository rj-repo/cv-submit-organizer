package org.rj.auth_service.application.user.exception;

import org.rj.cvsubmitorganizer.common.ResponseException;
import org.springframework.http.HttpStatus;

public class AuthUserNotFoundException extends ResponseException {

    private final String message;

    public AuthUserNotFoundException(String message) {
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
