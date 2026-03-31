package com.techwave.paymentservice.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.techwave.paymentservice.model.ExceptionDetail;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        ResponseEntity<ExceptionDetail> resp = handler.handleNotFound(
                new ResourceNotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals(404, resp.getBody().getStatus());
        assertEquals("Not found", resp.getBody().getMessage());
    }

    @Test
    void handleBadRequest_returns400() {
        ResponseEntity<ExceptionDetail> resp = handler.handleBadRequest(
                new BadRequestException("Bad input"));
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().getStatus());
    }

    @Test
    void handleUnauthorized_returns401() {
        ResponseEntity<ExceptionDetail> resp = handler.handleUnauthorized(
                new UnauthorizedException("No auth"));
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }

    @Test
    void handleForbidden_returns403() {
        ResponseEntity<ExceptionDetail> resp = handler.handleForbidden(
                new ForbiddenOperationException("Denied"));
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }

    @Test
    void handleGeneric_returns500() {
        ResponseEntity<ExceptionDetail> resp = handler.handleGeneric(
                new RuntimeException("boom"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals("An unexpected error occurred", resp.getBody().getMessage());
    }
}

