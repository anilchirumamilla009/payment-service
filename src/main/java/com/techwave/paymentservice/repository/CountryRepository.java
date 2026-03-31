package com.techwave.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.CountryEntity;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, String> {
}

