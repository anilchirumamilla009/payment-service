package com.techwave.paymentservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.CorporationAuditEntity;

@Repository
public interface CorporationAuditRepository extends JpaRepository<CorporationAuditEntity, Long> {

    List<CorporationAuditEntity> findByResourceOrderByVersionAsc(UUID resource);

    int countByResource(UUID resource);
}

