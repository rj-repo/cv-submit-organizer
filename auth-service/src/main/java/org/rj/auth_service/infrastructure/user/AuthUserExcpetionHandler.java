package org.rj.auth_service.infrastructure.user;


import lombok.extern.slf4j.Slf4j;
import org.rj.auth_service.domain.user.model.AuthUserDomainException;
import org.rj.cvsubmitorganizer.common.ApiResponseException;
import org.rj.cvsubmitorganizer.common.ResponseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice
@Slf4j
public class AuthUserExcpetionHandler {

    @ExceptionHandler({AuthUserDomainException.class})
    public ResponseEntity<ApiResponseException> handleResponseException(RuntimeException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiResponseException responseException = ApiResponseException.builder()
                .message(ex.getMessage())
                .statusCode(badRequest.value())
                .build();
        return ResponseEntity.status(badRequest).body(responseException);
    }

    @ExceptionHandler({ResponseException.class})
    public ResponseEntity<ApiResponseException> handleResponseException(ResponseException ex) {
        ApiResponseException responseException = ApiResponseException.builder()
                .message(ex.getMessage())
                .statusCode(ex.getHttpStatus().value())
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(responseException);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponseException> handleDefault(MethodArgumentNotValidException ex) {
        ApiResponseException responseException = ApiResponseException.builder()
                .messages(ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseException);
    }

    @ExceptionHandler({RestClientResponseException.class})
    public ResponseEntity<ApiResponseException> handleDefault(RestClientResponseException ex) {
        ApiResponseException responseException = ApiResponseException.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseException);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ApiResponseException> handleResponseException(AuthenticationException ex) {
        ApiResponseException responseException = ApiResponseException.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(responseException);
    }
}
