package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.BankAccountAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link BankAccountAuditEntity}.
 */
@Repository
public interface BankAccountAuditRepository
        extends JpaRepository<BankAccountAuditEntity, Long> {

    /**
     * Find all audit trail entries for a specific bank account resource,
     * ordered by version ascending.
     *
     * @param resource the UUID of the bank account
     * @return list of audit entries
     */
    List<BankAccountAuditEntity> findByResourceOrderByVersionAsc(
            UUID resource);
}

