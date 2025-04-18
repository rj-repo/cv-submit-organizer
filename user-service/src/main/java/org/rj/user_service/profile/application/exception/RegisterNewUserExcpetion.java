package org.rj.user_service.profile.application.exception;

import org.rj.cvsubmitorganizer.common.ResponseException;
import org.springframework.http.HttpStatus;

public class RegisterNewUserExcpetion extends ResponseException {


    public RegisterNewUserExcpetion(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
