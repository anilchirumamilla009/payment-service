package com.techwave.paymentservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.BeneficialOwnerEntity;

@Repository
public interface BeneficialOwnerRepository extends JpaRepository<BeneficialOwnerEntity, Long> {

    List<BeneficialOwnerEntity> findByAccountIdAndAccountType(UUID accountId, String accountType);
}

