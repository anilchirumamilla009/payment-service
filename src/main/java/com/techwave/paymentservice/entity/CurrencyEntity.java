package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing a currency record.
 * Maps to the 'currencies' table and stores ISO 4217 currency data.
 */
@Entity
@Table(name = "currencies")
public class CurrencyEntity {

    @Id
    @Column(name = "id", length = 3, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
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

