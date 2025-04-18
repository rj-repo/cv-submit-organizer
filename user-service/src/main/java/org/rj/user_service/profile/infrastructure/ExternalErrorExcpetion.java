package org.rj.user_service.profile.infrastructure;

import lombok.Getter;
import org.rj.cvsubmitorganizer.common.ApiResponseException;

public class ExternalErrorExcpetion extends RuntimeException {

    @Getter
    private final ApiResponseException apiResponse;

    public ExternalErrorExcpetion(ApiResponseException message) {
        this.apiResponse = message;
    }
}
