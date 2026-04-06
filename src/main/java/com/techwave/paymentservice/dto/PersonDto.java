package com.techwave.paymentservice.dto;

import java.util.UUID;

/**
 * DTO representing a person legal entity,
 * matching the OpenAPI Person schema.
 */
public class PersonDto {

    private UUID id;
    private String resourceType = "people";
    private String firstName;
    private String lastName;
    private UUID duplicates;

    public PersonDto() {
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

