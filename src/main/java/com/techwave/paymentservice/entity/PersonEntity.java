package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.UUID;

@Entity
@DiscriminatorValue("people")
public class PersonEntity extends LegalEntityEntity {
    
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;
    
    @Column(name = "duplicates", columnDefinition = "UUID")
    private UUID duplicates;
    
    public PersonEntity() {
    }
    
    public PersonEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
    
    public UUID getDuplicates() {
        return duplicates;
    }
    
    public void setDuplicates(UUID duplicates) {
        this.duplicates = duplicates;
    }
}
