package com.techwave.paymentservice.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class CorporationResponse {
    
    private UUID id;
    private String resourceType;
    private String name;
    private String code;
    private LocalDate incorporationDate;
    private String incorporationCountry;
    private String type;
    private UUID duplicates;
    
    public CorporationResponse() {
    }
    
    public CorporationResponse(UUID id, String name, String code, LocalDate incorporationDate,
                              String incorporationCountry, String type, UUID duplicates) {
        this.id = id;
        this.resourceType = "corporations";
        this.name = name;
        this.code = code;
        this.incorporationDate = incorporationDate;
        this.incorporationCountry = incorporationCountry;
        this.type = type;
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
