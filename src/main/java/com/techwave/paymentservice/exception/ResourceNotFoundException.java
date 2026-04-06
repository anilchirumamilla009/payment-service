package com.techwave.paymentservice.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Results in an HTTP 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceType;
    private final String resourceId;

    public ResourceNotFoundException(String resourceType,
                                     String resourceId) {
        super(String.format("%s not found with id: %s",
                resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}

