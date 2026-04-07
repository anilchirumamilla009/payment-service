package com.techwave.paymentservice.exception;

import com.techwave.paymentservice.dto.ExceptionDetailDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Unit Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    @DisplayName("handleResourceNotFound returns 404")
    void handleResourceNotFound_returns404() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Person", "abc-123");

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertTrue(response.getBody().getMessage()
                .contains("Person"));
    }

    @Test
    @DisplayName("handleBadRequest returns 400")
    void handleBadRequest_returns400() {
        BadRequestException ex =
                new BadRequestException("Invalid input");

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleBadRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    @DisplayName("handleValidationErrors returns 400 with field errors")
    void handleValidationErrors_returns400() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError(
                "personDto", "firstName", "First name is required");
        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Validation failed",
                response.getBody().getMessage());
        assertFalse(response.getBody().getMessages().isEmpty());
        assertTrue(response.getBody().getMessages().get(0)
                .contains("firstName"));
    }

    @Test
    @DisplayName("handleConstraintViolation returns 400")
    void handleConstraintViolation_returns400() {
        ConstraintViolation<?> violation = new ConstraintViolation<>() {
            @Override public String getMessage() { return "must not be blank"; }
            @Override public String getMessageTemplate() { return null; }
            @Override public Object getRootBean() { return null; }
            @Override public Class<Object> getRootBeanClass() { return null; }
            @Override public Object getLeafBean() { return null; }
            @Override public Object[] getExecutableParameters() { return null; }
            @Override public Object getExecutableReturnValue() { return null; }
            @Override public Path getPropertyPath() {
                return new Path() {
                    @Override public Iterator<Node> iterator() { return java.util.Collections.emptyIterator(); }
                    @Override public String toString() { return "name"; }
                };
            }
            @Override public Object getInvalidValue() { return null; }
            @Override public jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor() { return null; }
            @Override public <U> U unwrap(Class<U> type) { return null; }
        };

        ConstraintViolationException ex =
                new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Constraint violation",
                response.getBody().getMessage());
    }

    @Test
    @DisplayName("handleUnreadableMessage returns 400")
    void handleUnreadableMessage_returns400() {
        @SuppressWarnings("deprecation")
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Malformed JSON");

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleUnreadableMessage(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Malformed request body",
                response.getBody().getMessage());
    }

    @Test
    @DisplayName("handleMissingParam returns 400")
    void handleMissingParam_returns400() {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException(
                        "id", "String");

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleMissingParam(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
    }

    @Test
    @DisplayName("handleTypeMismatch returns 400")
    void handleTypeMismatch_returns400() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException(
                        "bad-value", java.util.UUID.class, "uuid",
                        null, new IllegalArgumentException("invalid"));

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("uuid"));
        assertTrue(response.getBody().getMessage().contains("UUID"));
    }

    @Test
    @DisplayName("handleTypeMismatch with null requiredType returns 400")
    void handleTypeMismatch_nullRequiredType() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException(
                        "bad-value", null, "param",
                        null, new IllegalArgumentException("invalid"));

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("unknown"));
    }

    @Test
    @DisplayName("handleMethodNotSupported returns 405")
    void handleMethodNotSupported_returns405() {
        HttpRequestMethodNotSupportedException ex =
                new HttpRequestMethodNotSupportedException("DELETE");

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleMethodNotSupported(ex);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED,
                response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(405, response.getBody().getStatus());
    }

    @Test
    @DisplayName("handleNoResourceFound returns 404")
    void handleNoResourceFound_returns404() {
        NoResourceFoundException ex =
                new NoResourceFoundException(
                        org.springframework.http.HttpMethod.GET,
                        "/api/v1/unknown");

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleNoResourceFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    @DisplayName("handleGenericException returns 500")
    void handleGenericException_returns500() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<ExceptionDetailDto> response =
                handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("An unexpected error occurred",
                response.getBody().getMessage());
    }
}




