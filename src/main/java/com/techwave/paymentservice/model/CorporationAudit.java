package com.techwave.paymentservice.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Audit representation for a corporation entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CorporationAudit {

    private UUID resource;
    private String resourceType;
    private Integer version;
    private String name;
    private String code;
    private LocalDate incorporationDate;
    private String incorporationCountry;
    private String type;
    private UUID duplicates;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getIncorporationDate() {
        return incorporationDate;
    }

    public void setIncorporationDate(LocalDate incorporationDate) {
        this.incorporationDate = incorporationDate;
    }

    public String getIncorporationCountry() {
        return incorporationCountry;
    }

    public void setIncorporationCountry(String incorporationCountry) {
        this.incorporationCountry = incorporationCountry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(UUID duplicates) {
        this.duplicates = duplicates;
    }
}

