package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link CountryEntity}.
 */
@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, String> {
}

