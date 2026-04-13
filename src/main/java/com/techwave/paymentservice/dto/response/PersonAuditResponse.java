package com.techwave.paymentservice.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class PersonAuditResponse {
    
    private UUID resource;
    private String resourceType;
    private Long version;
    private String firstName;
    private String lastName;
    private UUID duplicates;
    
    public PersonAuditResponse() {
    }
    
    public PersonAuditResponse(UUID resource, Long version, String firstName, String lastName, UUID duplicates) {
        this.resource = resource;
        this.resourceType = "person-audits";
        this.version = version;
        this.firstName = firstName;
        this.lastName = lastName;
        this.duplicates = duplicates;
    }
    
    public UUID getResource() {
        return resource;
    }
    
    public void setResource(UUID resource) {
        this.resource = resource;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public UUID getDuplicates() {
        return duplicates;
    }
    
    public void setDuplicates(UUID duplicates) {
        this.duplicates = duplicates;
    }
}
