package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CorporationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CorporationAuditRepository extends JpaRepository<CorporationAudit, Long> {
    List<CorporationAudit> findByResourceOrderByVersionDesc(UUID resourceId);
}
