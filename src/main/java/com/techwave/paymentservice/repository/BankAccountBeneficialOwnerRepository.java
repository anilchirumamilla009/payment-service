package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.BankAccountBeneficialOwner;
import com.techwave.paymentservice.entity.BankAccountBeneficialOwnerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface BankAccountBeneficialOwnerRepository 
    extends JpaRepository<BankAccountBeneficialOwner, BankAccountBeneficialOwnerId> {
    List<BankAccountBeneficialOwner> findByIdBankAccountId(UUID bankAccountId);
    List<BankAccountBeneficialOwner> findByIdLegalEntityId(UUID legalEntityId);
}
