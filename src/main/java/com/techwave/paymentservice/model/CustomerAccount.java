package com.techwave.paymentservice.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Customer account resource returned by the customer account API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerAccount {

    private UUID id;
    private String resourceType;
    private String name;
    private String description;
    private String accountType;
    private String accountState;
    private UUID accountManager;
    private OffsetDateTime accountCreationTime;
    private String silo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public UUID getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(UUID accountManager) {
        this.accountManager = accountManager;
    }

    public OffsetDateTime getAccountCreationTime() {
        return accountCreationTime;
    }

    public void setAccountCreationTime(OffsetDateTime accountCreationTime) {
        this.accountCreationTime = accountCreationTime;
    }

    public String getSilo() {
        return silo;
    }

    public void setSilo(String silo) {
        this.silo = silo;
    }
}

