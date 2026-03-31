package com.techwave.paymentservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.CorporationEntity;

@Repository
public interface CorporationRepository extends JpaRepository<CorporationEntity, UUID> {

    Optional<CorporationEntity> findByIncorporationCountryIgnoreCaseAndCodeIgnoreCase(String country, String code);
}

