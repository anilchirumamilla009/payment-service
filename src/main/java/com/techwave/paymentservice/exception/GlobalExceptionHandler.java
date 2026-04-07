package com.techwave.paymentservice.exception;

import com.techwave.paymentservice.dto.ExceptionDetailDto;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.Collections;
import java.util.List;

/**
 * Global exception handler that maps application and framework exceptions
 * to the ExceptionDetail schema defined in the OpenAPI specification.
 * Provides consistent error responses across all API endpoints.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles 404 – resource not found by id / bad lookup.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetailDto> handleResourceNotFound(
            ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                Collections.emptyList());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    /**
     * Handles 400 – explicit bad request thrown by service layer.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDetailDto> handleBadRequest(
            BadRequestException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                Collections.emptyList());
        return ResponseEntity.badRequest().body(detail);
    }

    /**
     * Handles 400 – bean validation failures on @Valid request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetailDto> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        log.warn("Validation failed: {}", messages);
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                messages);
        return ResponseEntity.badRequest().body(detail);
    }

    /**
     * Handles 400 – constraint violations from @Validated parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDetailDto> handleConstraintViolation(
            ConstraintViolationException ex) {
        List<String> messages = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();
        log.warn("Constraint violation: {}", messages);
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Constraint violation",
                messages);
        return ResponseEntity.badRequest().body(detail);
    }

    /**
     * Handles 400 – malformed JSON or unreadable request body.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDetailDto> handleUnreadableMessage(
            HttpMessageNotReadableException ex) {
        log.warn("Malformed request body: {}", ex.getMessage());
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Malformed request body",
                Collections.emptyList());
        return ResponseEntity.badRequest().body(detail);
    }

    /**
     * Handles 400 – missing required request parameters.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionDetailDto> handleMissingParam(
            MissingServletRequestParameterException ex) {
        log.warn("Missing parameter: {}", ex.getMessage());
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                Collections.emptyList());
        return ResponseEntity.badRequest().body(detail);
    }

    /**
     * Handles 400 – type mismatch (e.g., invalid UUID format).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDetailDto> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "Parameter '%s' must be of type %s",
                ex.getName(),
                ex.getRequiredType() != null
                        ? ex.getRequiredType().getSimpleName() : "unknown");
        log.warn("Type mismatch: {}", message);
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                Collections.emptyList());
        return ResponseEntity.badRequest().body(detail);
    }

    /**
     * Handles 405 – HTTP method not supported.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionDetailDto> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        log.warn("Method not supported: {}", ex.getMessage());
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                ex.getMessage(),
                Collections.emptyList());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(detail);
    }

    /**
     * Handles 404 – no resource found for the given path.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionDetailDto> handleNoResourceFound(
            NoResourceFoundException ex) {
        log.warn("No resource found: {}", ex.getMessage());
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                Collections.emptyList());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    /**
     * Handles 500 – catch-all for any unexpected server-side errors.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetailDto> handleGenericException(
            Exception ex) {
        log.error("Unexpected error occurred", ex);
        ExceptionDetailDto detail = buildDetail(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred",
                Collections.emptyList());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(detail);
    }

    private ExceptionDetailDto buildDetail(int status, String error,
                                           String message,
                                           List<String> messages) {
        ExceptionDetailDto detail = new ExceptionDetailDto();
        detail.setStatus(status);
        detail.setError(error);
        detail.setMessage(message);
        detail.setMessages(messages);
        return detail;
    }
}

