package com.techwave.paymentservice.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Base legal entity representation used by beneficial owner responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LegalEntity {

    private UUID id;
    private String resourceType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}

