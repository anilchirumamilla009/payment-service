package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link CurrencyEntity}.
 */
@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {
}

