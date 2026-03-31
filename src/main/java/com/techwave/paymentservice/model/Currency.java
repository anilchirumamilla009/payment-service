package com.techwave.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Currency reference data returned by the core API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Currency {

    private String id;
    private String resourceType;
    private String name;

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

