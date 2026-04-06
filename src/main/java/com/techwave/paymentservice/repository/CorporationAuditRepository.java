package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CorporationAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link CorporationAuditEntity}.
 */
@Repository
public interface CorporationAuditRepository
        extends JpaRepository<CorporationAuditEntity, Long> {

    /**
     * Find all audit trail entries for a specific corporation resource,
     * ordered by version ascending.
     *
     * @param resource the UUID of the corporation
     * @return list of audit entries
     */
    List<CorporationAuditEntity> findByResourceOrderByVersionAsc(
            UUID resource);
}

