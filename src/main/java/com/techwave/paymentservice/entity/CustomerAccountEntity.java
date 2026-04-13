package com.techwave.paymentservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customer_accounts", indexes = {
    @Index(name = "idx_cust_account_silo", columnList = "silo_id"),
    @Index(name = "idx_cust_account_manager", columnList = "account_manager_id")
})
public class CustomerAccountEntity {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "account_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CustomerAccountType accountType;
    
    @Column(name = "account_state", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CustomerAccountState accountState;
    
    @Column(name = "account_manager_id")
    private UUID accountManager;
    
    @CreatedDate
    @Column(name = "account_creation_time", nullable = false, updatable = false)
    private LocalDateTime accountCreationTime;
    
    @Column(name = "silo_id", length = 100)
    private String silo;
    
    @OneToMany(mappedBy = "customerAccount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CustomerAccountBeneficialOwner> beneficialOwners = new HashSet<>();
    
    @Version
    @Column(name = "version", nullable = false)
    private Long version;
    
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
    
    public Set<CustomerAccountBeneficialOwner> getBeneficialOwners() {
        return beneficialOwners;
    }
    
    public void setBeneficialOwners(Set<CustomerAccountBeneficialOwner> beneficialOwners) {
        this.beneficialOwners = beneficialOwners;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
}
