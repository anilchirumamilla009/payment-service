package com.techwave.paymentservice.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Error payload aligned with the OpenAPI service exception schema.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDetail {

    private Integer status;
    private String error;
    private String message;
    private List<String> messages = new ArrayList<>();

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

