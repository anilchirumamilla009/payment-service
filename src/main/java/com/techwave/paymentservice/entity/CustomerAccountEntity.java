package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA entity representing a customer account.
 * Supports beneficial owner relationships through a many-to-many
 * join with {@link LegalEntityBase}.
 */
@Entity
@Table(name = "customer_accounts")
public class CustomerAccountEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", length = 50)
    private CustomerAccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_state", length = 50)
    private CustomerAccountState accountState;

    @Column(name = "account_manager")
    private UUID accountManager;

    @Column(name = "account_creation_time")
    private LocalDateTime accountCreationTime;

    @Column(name = "silo", length = 100)
    private String silo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "customer_account_beneficial_owners",
        joinColumns = @JoinColumn(name = "customer_account_id"),
        inverseJoinColumns = @JoinColumn(name = "legal_entity_id")
    )
    private Set<LegalEntityBase> beneficialOwners = new HashSet<>();

    public CustomerAccountEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public CustomerAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(CustomerAccountType accountType) {
        this.accountType = accountType;
    }

    public CustomerAccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(CustomerAccountState accountState) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<LegalEntityBase> getBeneficialOwners() {
        return beneficialOwners;
    }

    public void setBeneficialOwners(Set<LegalEntityBase> beneficialOwners) {
        this.beneficialOwners = beneficialOwners;
    }

    /**
     * Enum representing the type of customer account.
     */
    public enum CustomerAccountType {
        PERSONAL,
        CORPORATE,
        CHARITY,
        AGENT,
        LIQUIDITY_PROVIDER,
        BANKER
    }

    /**
     * Enum representing the state of a customer account.
     */
    public enum CustomerAccountState {
        DATA_REQUIRED,
        UNDER_REVIEW,
        ACCEPTED,
        REJECTED,
        NOT_REQUIRED,
        LAPSED,
        INACTIVE,
        CLOSED
    }
}

