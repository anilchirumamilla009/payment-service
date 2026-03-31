package com.techwave.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.paymentservice.entity.SiloEntity;

@Repository
public interface SiloRepository extends JpaRepository<SiloEntity, String> {
}

