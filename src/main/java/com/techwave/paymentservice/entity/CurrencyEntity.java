package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currencies")
public class CurrencyEntity {
    @Id
    @Column(name = "code", length = 3)
    private String id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    public CurrencyEntity() {
    }
    
    public CurrencyEntity(String id, String name) {
        this.id = id;
        this.name = name;
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
}
