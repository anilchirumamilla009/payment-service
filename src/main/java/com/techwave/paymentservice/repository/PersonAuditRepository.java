package com.techwave.paymentservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.PersonAuditEntity;

@Repository
public interface PersonAuditRepository extends JpaRepository<PersonAuditEntity, Long> {

    List<PersonAuditEntity> findByResourceOrderByVersionAsc(UUID resource);

    int countByResource(UUID resource);
}

