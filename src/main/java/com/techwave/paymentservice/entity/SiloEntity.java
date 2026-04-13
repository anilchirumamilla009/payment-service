package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "silos")
public class SiloEntity {
    @Id
    @Column(name = "id", length = 100)
    private String id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(name = "default_base_currency", length = 3)
    private String defaultBaseCurrency;
    
    @Column(name = "default_credit_limit", precision = 20, scale = 2)
    private BigDecimal defaultCreditLimit;
    
    @Column(name = "default_profit_share", precision = 3, scale = 2)
    private BigDecimal defaultProfitShare;
    
    @Column(name = "silo_type", length = 50)
    @Enumerated(EnumType.STRING)
    private SiloType type;
    
    public SiloEntity() {
    }
    
    public SiloEntity(String id, String name, String description, String email,
                     String defaultBaseCurrency, BigDecimal defaultCreditLimit,
                     BigDecimal defaultProfitShare, SiloType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.email = email;
        this.defaultBaseCurrency = defaultBaseCurrency;
        this.defaultCreditLimit = defaultCreditLimit;
        this.defaultProfitShare = defaultProfitShare;
        this.type = type;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDefaultBaseCurrency() {
        return defaultBaseCurrency;
    }
    
    public void setDefaultBaseCurrency(String defaultBaseCurrency) {
        this.defaultBaseCurrency = defaultBaseCurrency;
    }
    
    public BigDecimal getDefaultCreditLimit() {
        return defaultCreditLimit;
    }
    
    public void setDefaultCreditLimit(BigDecimal defaultCreditLimit) {
        this.defaultCreditLimit = defaultCreditLimit;
    }
    
    public BigDecimal getDefaultProfitShare() {
        return defaultProfitShare;
    }
    
    public void setDefaultProfitShare(BigDecimal defaultProfitShare) {
        this.defaultProfitShare = defaultProfitShare;
    }
    
    public SiloType getType() {
        return type;
    }
    
    public void setType(SiloType type) {
        this.type = type;
    }
}
