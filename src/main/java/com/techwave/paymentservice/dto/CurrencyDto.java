package com.techwave.paymentservice.dto;

/**
 * DTO representing a currency, matching the OpenAPI Currency schema.
 */
public class CurrencyDto {

    private String id;
    private String resourceType = "currencies";
    private String name;

    public CurrencyDto() {
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

