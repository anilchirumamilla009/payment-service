package com.techwave.paymentservice.dto.response;

import java.math.BigDecimal;

public class SiloResponse {
    private String id;
    private String resourceType;
    private String name;
    private String description;
    private String email;
    private String defaultBaseCurrency;
    private BigDecimal defaultCreditLimit;
    private BigDecimal defaultProfitShare;
    private String type;
    
    public SiloResponse() {
    }
    
    public SiloResponse(String id, String name, String description, String email,
                       String defaultBaseCurrency, BigDecimal defaultCreditLimit,
                       BigDecimal defaultProfitShare, String type) {
        this.id = id;
        this.resourceType = "silos";
        this.name = name;
        this.description = description;
        this.email = email;
        this.defaultBaseCurrency = defaultBaseCurrency;
        this.defaultCreditLimit = defaultCreditLimit;
        this.defaultProfitShare = defaultProfitShare;
        this.type = type;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDefaultBaseCurrency() {
        return defaultBaseCurrency;
    }
    
    public void setDefaultBaseCurrency(String defaultBaseCurrency) {
        this.defaultBaseCurrency = defaultBaseCurrency;
    }
    
    public BigDecimal getDefaultCreditLimit() {
        return defaultCreditLimit;
    }
    
    public void setDefaultCreditLimit(BigDecimal defaultCreditLimit) {
        this.defaultCreditLimit = defaultCreditLimit;
    }
    
    public BigDecimal getDefaultProfitShare() {
        return defaultProfitShare;
    }
    
    public void setDefaultProfitShare(BigDecimal defaultProfitShare) {
        this.defaultProfitShare = defaultProfitShare;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
