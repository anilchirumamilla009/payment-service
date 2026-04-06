package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

/**
 * JPA entity representing a corporation legal entity.
 * Discriminator value 'corporations' corresponds to the OpenAPI resourceType.
 */
@Entity
@DiscriminatorValue("corporations")
public class CorporationEntity extends LegalEntityBase {

    @Column(name = "name")
    private String name;

    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;

    @Column(name = "incorporation_country", length = 2)
    private String incorporationCountry;

    @Column(name = "type", length = 100)
    private String type;

    public CorporationEntity() {
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
}

