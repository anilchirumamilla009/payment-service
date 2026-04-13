package com.techwave.paymentservice.dto.response;

public class CurrencyResponse {
    private String id;
    private String resourceType;
    private String name;
    
    public CurrencyResponse() {
    }
    
    public CurrencyResponse(String id, String name) {
        this.id = id;
        this.resourceType = "currencies";
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
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
}
