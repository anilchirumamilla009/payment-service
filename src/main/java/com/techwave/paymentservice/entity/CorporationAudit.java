package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "corporation_audits")
public class CorporationAudit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resource_id", nullable = false)
    private UUID resource;
    
    @Column(name = "version", nullable = false)
    private Long version;
    
    @Column(name = "name", length = 255)
    private String name;
    
    @Column(name = "code", length = 100)
    private String code;
    
    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;
    
    @Column(name = "incorporation_country", length = 2)
    private String incorporationCountry;
    
    @Column(name = "corporation_type", length = 255)
    private String type;
    
    @Column(name = "duplicates")
    private UUID duplicates;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public CorporationAudit() {
    }
    
    public CorporationAudit(UUID resource, Long version, String name, String code,
                           LocalDate incorporationDate, String incorporationCountry,
                           String type, UUID duplicates) {
        this.resource = resource;
        this.version = version;
        this.name = name;
        this.code = code;
        this.incorporationDate = incorporationDate;
        this.incorporationCountry = incorporationCountry;
        this.type = type;
        this.duplicates = duplicates;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UUID getResource() {
        return resource;
    }
    
    public void setResource(UUID resource) {
        this.resource = resource;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public LocalDate getIncorporationDate() {
        return incorporationDate;
    }
    
    public void setIncorporationDate(LocalDate incorporationDate) {
        this.incorporationDate = incorporationDate;
    }
    
    public String getIncorporationCountry() {
        return incorporationCountry;
    }
    
    public void setIncorporationCountry(String incorporationCountry) {
        this.incorporationCountry = incorporationCountry;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public UUID getDuplicates() {
        return duplicates;
    }
    
    public void setDuplicates(UUID duplicates) {
        this.duplicates = duplicates;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
