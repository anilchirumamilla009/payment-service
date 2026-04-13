package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class BankAccountBeneficialOwnerId implements Serializable {
    
    @Column(name = "bank_account_id")
    private UUID bankAccountId;
    
    @Column(name = "legal_entity_id")
    private UUID legalEntityId;
    
    public BankAccountBeneficialOwnerId() {
    }
    
    public BankAccountBeneficialOwnerId(UUID bankAccountId, UUID legalEntityId) {
        this.bankAccountId = bankAccountId;
        this.legalEntityId = legalEntityId;
    }
    
    public UUID getBankAccountId() {
        return bankAccountId;
    }
    
    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
    
    public UUID getLegalEntityId() {
        return legalEntityId;
    }
    
    public void setLegalEntityId(UUID legalEntityId) {
        this.legalEntityId = legalEntityId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountBeneficialOwnerId that = (BankAccountBeneficialOwnerId) o;
        return Objects.equals(bankAccountId, that.bankAccountId) &&
                Objects.equals(legalEntityId, that.legalEntityId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bankAccountId, legalEntityId);
    }
}
