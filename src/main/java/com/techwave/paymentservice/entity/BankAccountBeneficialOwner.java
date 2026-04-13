package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_account_beneficial_owners", indexes = {
    @Index(name = "idx_ba_bo_legal_entity", columnList = "legal_entity_id")
})
public class BankAccountBeneficialOwner {
    
    @EmbeddedId
    private BankAccountBeneficialOwnerId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bankAccountId")
    @JoinColumn(name = "bank_account_id")
    private BankAccountEntity bankAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("legalEntityId")
    @JoinColumn(name = "legal_entity_id")
    private LegalEntityEntity legalEntity;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public BankAccountBeneficialOwner() {
    }
    
    public BankAccountBeneficialOwnerId getId() {
        return id;
    }
    
    public void setId(BankAccountBeneficialOwnerId id) {
        this.id = id;
    }
    
    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }
    
    public void setBankAccount(BankAccountEntity bankAccount) {
        this.bankAccount = bankAccount;
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
