package com.techwave.paymentservice.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class CorporationAuditResponse {
    
    private UUID resource;
    private String resourceType;
    private Long version;
    private String name;
    private String code;
    private LocalDate incorporationDate;
    private String incorporationCountry;
    private String type;
    private UUID duplicates;
    
    public CorporationAuditResponse() {
    }
    
    public CorporationAuditResponse(UUID resource, Long version, String name, String code,
                                   LocalDate incorporationDate, String incorporationCountry,
                                   String type, UUID duplicates) {
        this.resource = resource;
        this.resourceType = "corporation-audits";
        this.version = version;
        this.name = name;
        this.code = code;
        this.incorporationDate = incorporationDate;
        this.incorporationCountry = incorporationCountry;
        this.type = type;
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
