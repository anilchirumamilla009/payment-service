package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries", indexes = {
    @Index(name = "idx_country_alpha3", columnList = "alpha3_code"),
    @Index(name = "idx_country_numeric", columnList = "numeric_code")
})
public class CountryEntity {
    @Id
    @Column(name = "code", length = 2)
    private String id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "numeric_code", nullable = false, length = 3)
    private String numericCode;
    
    @Column(name = "alpha3_code", nullable = false, length = 3)
    private String alpha3Code;
    
    @Column(name = "is_eurozone", nullable = false)
    private boolean eurozone;
    
    @Column(name = "is_sepa", nullable = false)
    private boolean sepa;
    
    public CountryEntity() {
    }
    
    public CountryEntity(String id, String name, String numericCode, String alpha3Code,
                        boolean eurozone, boolean sepa) {
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
