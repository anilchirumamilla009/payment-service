package com.techwave.paymentservice.dto.response;

public class CountryResponse {
    private String id;
    private String resourceType;
    private String name;
    private String numericCode;
    private String alpha3Code;
    private boolean eurozone;
    private boolean sepa;
    
    public CountryResponse() {
    }
    
    public CountryResponse(String id, String name, String numericCode, String alpha3Code, 
                          boolean eurozone, boolean sepa) {
        this.id = id;
        this.resourceType = "countries";
        this.name = name;
        this.numericCode = numericCode;
        this.alpha3Code = alpha3Code;
        this.eurozone = eurozone;
        this.sepa = sepa;
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
    
    public String getNumericCode() {
        return numericCode;
    }
    
    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }
    
    public String getAlpha3Code() {
        return alpha3Code;
    }
    
    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }
    
    public boolean isEurozone() {
        return eurozone;
    }
    
    public void setEurozone(boolean eurozone) {
        this.eurozone = eurozone;
    }
    
    public boolean isSepa() {
        return sepa;
    }
    
    public void setSepa(boolean sepa) {
        this.sepa = sepa;
    }
}
