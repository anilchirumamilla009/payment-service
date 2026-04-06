package com.techwave.paymentservice.dto;

import java.util.List;

/**
 * DTO matching the OpenAPI ExceptionDetail schema.
 * Used by the global exception handler to return consistent
 * error responses across all API endpoints.
 */
public class ExceptionDetailDto {

    private Integer status;
    private String error;
    private String message;
    private List<String> messages;

    public ExceptionDetailDto() {
    }

    public ExceptionDetailDto(Integer status, String error,
                              String message, List<String> messages) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.messages = messages;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}

