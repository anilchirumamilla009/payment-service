package com.techwave.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.CurrencyEntity;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {
}

