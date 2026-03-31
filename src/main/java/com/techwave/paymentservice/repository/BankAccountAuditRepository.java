package com.techwave.paymentservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.BankAccountAuditEntity;

@Repository
public interface BankAccountAuditRepository extends JpaRepository<BankAccountAuditEntity, Long> {

    List<BankAccountAuditEntity> findByResourceOrderByVersionAsc(UUID resource);

    int countByResource(UUID resource);
}

