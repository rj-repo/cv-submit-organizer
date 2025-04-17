package org.rj.auth_service.application.user.exception;

import org.rj.cvsubmitorganizer.common.ResponseException;
import org.springframework.http.HttpStatus;

public class InvalidJwtTokenException extends ResponseException {

    private final String message;

    public InvalidJwtTokenException(String message) {
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

   
}
