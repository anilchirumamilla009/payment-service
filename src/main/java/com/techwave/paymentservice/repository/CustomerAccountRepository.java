package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CustomerAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link CustomerAccountEntity}.
 */
@Repository
public interface CustomerAccountRepository
        extends JpaRepository<CustomerAccountEntity, UUID> {
}

