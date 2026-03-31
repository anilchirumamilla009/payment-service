package com.techwave.paymentservice.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "person_audits")
public class PersonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID resource;

    @Column(nullable = false)
    private Integer version;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private UUID duplicates;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getResource() { return resource; }
    public void setResource(UUID resource) { this.resource = resource; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public UUID getDuplicates() { return duplicates; }
    public void setDuplicates(UUID duplicates) { this.duplicates = duplicates; }
}

