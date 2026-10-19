package com.stainclo.common.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        ErrorCode errorCode,
        String message,
        String path,
        List<FieldError> errors
) {
    public record FieldError(String field, String message){}
}
