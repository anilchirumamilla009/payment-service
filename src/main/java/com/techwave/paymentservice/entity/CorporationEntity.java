package com.techwave.paymentservice.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "corporations")
public class CorporationEntity {

    @Id
    private UUID id;

    @Column(length = 255)
    private String name;

    @Column(length = 50)
    private String code;

    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;

    @Column(name = "incorporation_country", length = 2)
    private String incorporationCountry;

    @Column(length = 50)
    private String type;

    private UUID duplicates;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDate getIncorporationDate() { return incorporationDate; }
    public void setIncorporationDate(LocalDate incorporationDate) { this.incorporationDate = incorporationDate; }

    public String getIncorporationCountry() { return incorporationCountry; }
    public void setIncorporationCountry(String incorporationCountry) { this.incorporationCountry = incorporationCountry; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public UUID getDuplicates() { return duplicates; }
    public void setDuplicates(UUID duplicates) { this.duplicates = duplicates; }
}

