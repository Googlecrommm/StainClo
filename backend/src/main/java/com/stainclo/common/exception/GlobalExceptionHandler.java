package com.stainclo.common.exception;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(
            ErrorCode errorCode,
            String message,
            HttpServletRequest request,
            List<ErrorResponse.FieldError> fieldErrors
    ) {
        return new ErrorResponse(
                Instant.now(),
                errorCode,
                message,
                request.getRequestURI(),
                fieldErrors
        );
    }

    //APPLICATION EXCEPTION
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception, HttpServletRequest request){
        ErrorResponse body = buildErrorResponse(
          exception.getErrorCode(),
          exception.getMessage(),
          request,
          List.of()
        );

        return ResponseEntity.status(exception.getErrorCode().getStatus()).body(body);
    }

    //VALIDATION EXCEPTION
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ){
        List<ErrorResponse.FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        ErrorResponse body = buildErrorResponse(
               ErrorCode.VALIDATION_FAILED,
                "Request validation failed",
                request,
                fieldErrors
        );

        return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.getStatus()).body(body);
    }

    //UNREADABLE EXCEPTION
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        ErrorResponse body = buildErrorResponse(
                ErrorCode.MALFORMED_REQUEST_BODY,
                "Request body is missing or malformed",
                request,
                List.of()
        );
        return ResponseEntity.status(ErrorCode.MALFORMED_REQUEST_BODY.getStatus()).body(body);
    }

    //DATAINTEGRITYVIOLATION EXCEPTION
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        ErrorResponse body = buildErrorResponse(
                ErrorCode.DATA_CONFLICT,
                "The request conflicts with existing data.",
                request,
                List.of()
        );
        return ResponseEntity.status(ErrorCode.DATA_CONFLICT.getStatus()).body(body);
    }
}
