package com.techwave.paymentservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerAccountResponse {
    
    private UUID id;
    private String resourceType;
    private String name;
    private String description;
    private String accountType;
    private String accountState;
    private UUID accountManager;
    private LocalDateTime accountCreationTime;
    private String silo;
    
    public CustomerAccountResponse() {
    }
    
    public CustomerAccountResponse(UUID id, String name, String description, String accountType,
                                   String accountState, UUID accountManager, LocalDateTime accountCreationTime,
                                   String silo) {
        this.id = id;
        this.resourceType = "customer-accounts";
        this.name = name;
        this.description = description;
        this.accountType = accountType;
        this.accountState = accountState;
        this.accountManager = accountManager;
        this.accountCreationTime = accountCreationTime;
        this.silo = silo;
    }
    
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
    
    public LocalDateTime getAccountCreationTime() {
        return accountCreationTime;
    }
    
    public void setAccountCreationTime(LocalDateTime accountCreationTime) {
        this.accountCreationTime = accountCreationTime;
    }
    
    public String getSilo() {
        return silo;
    }
    
    public void setSilo(String silo) {
        this.silo = silo;
    }
}
