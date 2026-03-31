package com.techwave.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Country reference data returned by the core API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Country {

    private String id;
    private String resourceType;
    private String name;
    private String numericCode;
    private String alpha3Code;
    private Boolean eurozone;
    private Boolean sepa;

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

    public Boolean getEurozone() {
        return eurozone;
    }

    public void setEurozone(Boolean eurozone) {
        this.eurozone = eurozone;
    }

    public Boolean getSepa() {
        return sepa;
    }

    public void setSepa(Boolean sepa) {
        this.sepa = sepa;
    }
}

