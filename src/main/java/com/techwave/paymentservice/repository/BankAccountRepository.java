package com.techwave.paymentservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.BankAccountEntity;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {
}

