package com.techwave.paymentservice.dto;

import java.util.UUID;

/**
 * DTO representing a legal entity (base type for Person and Corporation),
 * matching the OpenAPI LegalEntity schema.
 */
public class LegalEntityDto {

    private UUID id;
    private String resourceType;

    public LegalEntityDto() {
    }

    public LegalEntityDto(UUID id, String resourceType) {
        this.id = id;
        this.resourceType = resourceType;
    }

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

