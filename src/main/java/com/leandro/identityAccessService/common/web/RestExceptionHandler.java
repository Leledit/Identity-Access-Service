package com.leandro.identityAccessService.common.web;

import com.leandro.identityAccessService.common.exception.ErrorCode;
import com.leandro.identityAccessService.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        List<String> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        List<String> allMessages = fieldErrors.stream()
                .map(f -> f.getField() + ": " + f.getMessage())
                .toList();

        log.warn("Validation failed: {}", allMessages);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.VALIDATION_ERROR.getCode())
                .message(ErrorCode.VALIDATION_ERROR.getMessage())
                .errors(fieldErrors.isEmpty() ? globalErrors.stream()
                        .map(m -> new FieldErrorDetail(null, m)).toList()
                        : fieldErrors)
                .build();

        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.UNAUTHORIZED.getCode())
                .message("Invalid username or password")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.warn("Bad request: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    protected ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {
        log.warn("Resource already exists: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {
        log.warn("Concurrent modification conflict: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.warn("Invalid request argument: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.VALIDATION_ERROR.getCode())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred", ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_ERROR.getCode())
                .message(ErrorCode.INTERNAL_ERROR.getMessage())
                .build();
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, ApiException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String code;
        private String message;
        private List<FieldErrorDetail> errors;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String message;
    }
}
