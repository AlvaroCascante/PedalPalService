package com.quetoquenana.pedalpal.common.exception;

import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    private final MessageSource messageSource;

    public ControllerExceptionAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(
            BadRequestException ex, Locale locale) {
        log.warn("BadRequestException: {}", ex.getMessage());
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
        return ResponseEntity.badRequest().body(new ApiResponse(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(
            BusinessException ex, Locale locale) {
        log.warn("BusinessException: {}", ex.getMessage());
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
        return ResponseEntity.internalServerError().body(new ApiResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(
            DomainException ex, Locale locale) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        String message = messageSource.getMessage("bad.request", null, locale);
        return ResponseEntity.internalServerError().body(new ApiResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse> handleDomainException(
            DomainException ex, Locale locale) {
        log.warn("DomainException: {}", ex.getMessage());
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
        return ResponseEntity.badRequest().body(new ApiResponse(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ApiResponse> handleForbiddenAccessExceptionException(
            ForbiddenAccessException ex, Locale locale) {
        log.error("ForbiddenAccessException: {}", ex.getMessage());
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(message, HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApiResponse> handleRecordNotFoundException(
            RecordNotFoundException ex, Locale locale) {
        log.error("RecordNotFoundException: {}", ex.getMessage());
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(message, HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        BindingResult bindingResult = ex.getBindingResult();
        String details = bindingResult.getFieldErrors().stream()
                .map(fieldError -> messageSource.getMessage(fieldError, locale))
                .collect(Collectors.joining("; "));
        String summary = messageSource.getMessage("validation.failed", null, locale);
        String full = summary + ": " + details;
        log.warn("Validation failed: {}", full);
        return ResponseEntity.badRequest().body(new ApiResponse(full, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex, Locale locale) {
        log.warn("Access denied: {}", ex.getMessage());
        String message = messageSource.getMessage("authentication.required", null, locale);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(message, HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            Locale locale
    ) {
        log.warn("Method argument type mismatch: {}", ex.getMessage());
        String message = messageSource.getMessage("bad.request", null, locale);
        return ResponseEntity.badRequest().body(new ApiResponse(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex, Locale locale) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        String message = messageSource.getMessage("unexpected.error", null, locale);
        return ResponseEntity.internalServerError().body(new ApiResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
