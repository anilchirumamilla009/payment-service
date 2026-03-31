package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currencies")
public class CurrencyEntity {

    @Id
    @Column(length = 3)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

