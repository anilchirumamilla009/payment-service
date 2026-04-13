package com.techwave.paymentservice.dto.response;

import java.util.UUID;

public class LegalEntityResponse {
    
    private UUID id;
    private String resourceType;
    
    public LegalEntityResponse() {
    }
    
    public LegalEntityResponse(UUID id, String resourceType) {
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
