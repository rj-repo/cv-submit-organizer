package org.rj.cvsubmitorganizer.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseException(int statusCode, String message, List<String> messages, UUID errorId) {

}
