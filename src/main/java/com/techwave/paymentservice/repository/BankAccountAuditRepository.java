package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.BankAccountAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface BankAccountAuditRepository extends JpaRepository<BankAccountAudit, Long> {
    List<BankAccountAudit> findByResourceOrderByVersionDesc(UUID resourceId);
}
