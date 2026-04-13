package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.LegalEntityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface LegalEntityRepository extends JpaRepository<LegalEntityEntity, UUID> {
}
