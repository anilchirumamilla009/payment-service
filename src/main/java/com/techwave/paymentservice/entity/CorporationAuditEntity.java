package com.techwave.paymentservice.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "corporation_audits")
public class CorporationAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID resource;

    @Column(nullable = false)
    private Integer version;

    private String name;
    private String code;
    @Column(name = "incorporation_date") private LocalDate incorporationDate;
    @Column(name = "incorporation_country") private String incorporationCountry;
    private String type;
    private UUID duplicates;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getResource() { return resource; }
    public void setResource(UUID resource) { this.resource = resource; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

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

