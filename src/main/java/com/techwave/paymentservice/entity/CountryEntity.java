package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing a country record.
 * Maps to the 'countries' table and stores ISO 3166-1 country data.
 */
@Entity
@Table(name = "countries")
public class CountryEntity {

    @Id
    @Column(name = "id", length = 2, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "numeric_code", length = 3)
    private String numericCode;

    @Column(name = "alpha3_code", length = 3)
    private String alpha3Code;

    @Column(name = "eurozone")
    private Boolean eurozone;

    @Column(name = "sepa")
    private Boolean sepa;

    public CountryEntity() {
    }

    public CountryEntity(String id, String name, String numericCode,
                         String alpha3Code, Boolean eurozone, Boolean sepa) {
        this.id = id;
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

