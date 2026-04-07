package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * JPA entity representing a silo record.
 * A silo is an organizational unit such as a treasury, business unit, subsidiary, or agent.
 */
@Entity
@Table(name = "silos")
public class SiloEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "default_base_currency", length = 3)
    private String defaultBaseCurrency;

    @Column(name = "default_credit_limit", precision = 19, scale = 4)
    private BigDecimal defaultCreditLimit;

    @Column(name = "default_profit_share", precision = 5, scale = 4)
    private BigDecimal defaultProfitShare;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private SiloType type;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiloEntity that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Enum representing the type of silo.
     */
    public enum SiloType {
        TREASURY,
        BUSINESS_UNIT,
        SUBSIDIARY,
        AGENT
    }
}

