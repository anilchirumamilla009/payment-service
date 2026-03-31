package com.techwave.paymentservice.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Person legal entity defined by the API contract.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person extends LegalEntity {

    private String firstName;
    private String lastName;
    private UUID duplicates;

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

    public UUID getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(UUID duplicates) {
        this.duplicates = duplicates;
    }
}

