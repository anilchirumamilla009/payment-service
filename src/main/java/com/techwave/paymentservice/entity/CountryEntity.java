package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class CountryEntity {

    @Id
    @Column(length = 2)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "numeric_code", length = 3)
    private String numericCode;

    @Column(name = "alpha3_code", length = 3)
    private String alpha3Code;

    private Boolean eurozone;
    private Boolean sepa;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNumericCode() { return numericCode; }
    public void setNumericCode(String numericCode) { this.numericCode = numericCode; }

    public String getAlpha3Code() { return alpha3Code; }
    public void setAlpha3Code(String alpha3Code) { this.alpha3Code = alpha3Code; }

    public Boolean getEurozone() { return eurozone; }
    public void setEurozone(Boolean eurozone) { this.eurozone = eurozone; }

    public Boolean getSepa() { return sepa; }
    public void setSepa(Boolean sepa) { this.sepa = sepa; }
}

