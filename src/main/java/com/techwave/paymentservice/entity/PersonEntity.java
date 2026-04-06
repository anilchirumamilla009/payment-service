package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * JPA entity representing a person (natural person) legal entity.
 * Discriminator value 'people' corresponds to the OpenAPI resourceType.
 */
@Entity
@DiscriminatorValue("people")
public class PersonEntity extends LegalEntityBase {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public PersonEntity() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

