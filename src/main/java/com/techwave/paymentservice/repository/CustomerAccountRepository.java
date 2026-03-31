package com.techwave.paymentservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.CustomerAccountEntity;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccountEntity, UUID> {
}

