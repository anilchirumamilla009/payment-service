package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PersonEntity}.
 */
@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {
}

