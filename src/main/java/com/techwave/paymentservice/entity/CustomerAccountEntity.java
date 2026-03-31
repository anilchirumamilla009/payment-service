package com.techwave.paymentservice.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_accounts")
public class CustomerAccountEntity {

    @Id
    private UUID id;

    @Column(length = 255)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "account_type", length = 30)
    private String accountType;

    @Column(name = "account_state", length = 30)
    private String accountState;

    @Column(name = "account_manager")
    private UUID accountManager;

    @Column(name = "account_creation_time")
    private OffsetDateTime accountCreationTime;

    @Column(length = 50)
    private String silo;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getAccountState() { return accountState; }
    public void setAccountState(String accountState) { this.accountState = accountState; }

    public UUID getAccountManager() { return accountManager; }
    public void setAccountManager(UUID accountManager) { this.accountManager = accountManager; }

    public OffsetDateTime getAccountCreationTime() { return accountCreationTime; }
    public void setAccountCreationTime(OffsetDateTime accountCreationTime) { this.accountCreationTime = accountCreationTime; }

    public String getSilo() { return silo; }
    public void setSilo(String silo) { this.silo = silo; }
}

