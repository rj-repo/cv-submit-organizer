package org.rj.cvsubmitorganizer.common;

import org.springframework.http.HttpStatus;

public abstract class ResponseException extends RuntimeException {

    public ResponseException() {
    }

    public ResponseException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public String getClassName() {
        return getClass().getSimpleName();
    }

}
