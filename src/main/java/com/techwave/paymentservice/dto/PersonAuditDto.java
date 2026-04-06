package com.techwave.paymentservice.dto;

import java.util.UUID;

/**
 * DTO representing a person audit trail entry,
 * matching the OpenAPI PersonAudit schema.
 */
public class PersonAuditDto {

    private UUID resource;
    private String resourceType = "person-audits";
    private Integer version;
    private String firstName;
    private String lastName;
    private UUID duplicates;

    public PersonAuditDto() {
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
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

