package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CorporationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorporationRepository extends JpaRepository<CorporationEntity, UUID> {
    Optional<CorporationEntity> findByCodeAndIncorporationCountry(String code, String country);
    List<CorporationEntity> findByIncorporationCountry(String country);
}
