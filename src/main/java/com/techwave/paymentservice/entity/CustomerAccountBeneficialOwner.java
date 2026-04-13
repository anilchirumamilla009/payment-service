package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_account_beneficial_owners")
public class CustomerAccountBeneficialOwner {
    
    @EmbeddedId
    private CustomerAccountBeneficialOwnerId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerAccountId")
    @JoinColumn(name = "customer_account_id")
    private CustomerAccountEntity customerAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("legalEntityId")
    @JoinColumn(name = "legal_entity_id")
    private LegalEntityEntity legalEntity;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public CustomerAccountBeneficialOwner() {
    }
    
    public CustomerAccountBeneficialOwnerId getId() {
        return id;
    }
    
    public void setId(CustomerAccountBeneficialOwnerId id) {
        this.id = id;
    }
    
    public CustomerAccountEntity getCustomerAccount() {
        return customerAccount;
    }
    
    public void setCustomerAccount(CustomerAccountEntity customerAccount) {
        this.customerAccount = customerAccount;
    }
    
    public LegalEntityEntity getLegalEntity() {
        return legalEntity;
    }
    
    public void setLegalEntity(LegalEntityEntity legalEntity) {
        this.legalEntity = legalEntity;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
