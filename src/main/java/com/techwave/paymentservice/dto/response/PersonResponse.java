package com.techwave.paymentservice.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class PersonResponse {
    
    private UUID id;
    private String resourceType;
    private String firstName;
    private String lastName;
    private UUID duplicates;
    
    public PersonResponse() {
    }
    
    public PersonResponse(UUID id, String firstName, String lastName, UUID duplicates) {
        this.id = id;
        this.resourceType = "people";
        this.firstName = firstName;
        this.lastName = lastName;
        this.duplicates = duplicates;
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
