package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.SiloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link SiloEntity}.
 */
@Repository
public interface SiloRepository extends JpaRepository<SiloEntity, String> {
}

