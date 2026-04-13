package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CustomerAccountBeneficialOwner;
import com.techwave.paymentservice.entity.CustomerAccountBeneficialOwnerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerAccountBeneficialOwnerRepository 
    extends JpaRepository<CustomerAccountBeneficialOwner, CustomerAccountBeneficialOwnerId> {
    List<CustomerAccountBeneficialOwner> findByIdCustomerAccountId(UUID customerAccountId);
}
