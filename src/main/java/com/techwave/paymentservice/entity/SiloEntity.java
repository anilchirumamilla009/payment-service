package com.techwave.paymentservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "silos")
public class SiloEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String email;

    @Column(name = "default_base_currency", length = 3)
    private String defaultBaseCurrency;

    @Column(name = "default_credit_limit", precision = 19, scale = 2)
    private BigDecimal defaultCreditLimit;

    @Column(name = "default_profit_share", precision = 5, scale = 4)
    private BigDecimal defaultProfitShare;

    @Column(length = 30)
    private String type;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDefaultBaseCurrency() { return defaultBaseCurrency; }
    public void setDefaultBaseCurrency(String defaultBaseCurrency) { this.defaultBaseCurrency = defaultBaseCurrency; }

    public BigDecimal getDefaultCreditLimit() { return defaultCreditLimit; }
    public void setDefaultCreditLimit(BigDecimal defaultCreditLimit) { this.defaultCreditLimit = defaultCreditLimit; }

    public BigDecimal getDefaultProfitShare() { return defaultProfitShare; }
    public void setDefaultProfitShare(BigDecimal defaultProfitShare) { this.defaultProfitShare = defaultProfitShare; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

