package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.PersonAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PersonAuditRepository extends JpaRepository<PersonAudit, Long> {
    List<PersonAudit> findByResourceOrderByVersionDesc(UUID resourceId);
}
