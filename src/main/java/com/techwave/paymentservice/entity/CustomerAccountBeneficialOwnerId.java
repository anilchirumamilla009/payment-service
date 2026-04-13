package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class CustomerAccountBeneficialOwnerId implements Serializable {
    
    @Column(name = "customer_account_id")
    private UUID customerAccountId;
    
    @Column(name = "legal_entity_id")
    private UUID legalEntityId;
    
    public CustomerAccountBeneficialOwnerId() {
    }
    
    public CustomerAccountBeneficialOwnerId(UUID customerAccountId, UUID legalEntityId) {
        this.customerAccountId = customerAccountId;
        this.legalEntityId = legalEntityId;
    }
    
    public UUID getCustomerAccountId() {
        return customerAccountId;
    }
    
    public void setCustomerAccountId(UUID customerAccountId) {
        this.customerAccountId = customerAccountId;
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
        CustomerAccountBeneficialOwnerId that = (CustomerAccountBeneficialOwnerId) o;
        return Objects.equals(customerAccountId, that.customerAccountId) &&
                Objects.equals(legalEntityId, that.legalEntityId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerAccountId, legalEntityId);
    }
}
