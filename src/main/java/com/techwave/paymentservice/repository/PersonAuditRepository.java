package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.PersonAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PersonAuditEntity}.
 */
@Repository
public interface PersonAuditRepository
        extends JpaRepository<PersonAuditEntity, Long> {

    /**
     * Find all audit trail entries for a specific person resource,
     * ordered by version ascending.
     *
     * @param resource the UUID of the person
     * @return list of audit entries
     */
    List<PersonAuditEntity> findByResourceOrderByVersionAsc(UUID resource);
}

