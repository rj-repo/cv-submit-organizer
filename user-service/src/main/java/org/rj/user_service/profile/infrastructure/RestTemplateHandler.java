package org.rj.user_service.profile.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.rj.cvsubmitorganizer.common.ApiResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.InputStream;
import java.util.UUID;

@Component
@Slf4j
public class RestTemplateHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) {
        final ObjectMapper objectMapper = new ObjectMapper();
        ApiResponseException apiError;
        try (InputStream body = response.getBody()) {
            apiError = objectMapper.readValue(body, ApiResponseException.class);
            throw new ExternalErrorExcpetion(apiError);
        } catch (Exception e) {
            String unexpectedError = "An unexpected error occurred while processing the response.";
            apiError = ApiResponseException.builder()
                    .message(unexpectedError)
                    .errorId(UUID.randomUUID())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            throw new ExternalErrorExcpetion(apiError);
        }

    }

}
