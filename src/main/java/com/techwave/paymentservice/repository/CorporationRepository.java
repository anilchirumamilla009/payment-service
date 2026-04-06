package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CorporationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link CorporationEntity}.
 */
@Repository
public interface CorporationRepository
        extends JpaRepository<CorporationEntity, UUID> {

    /**
     * Find a corporation by its incorporation country and company code.
     *
     * @param incorporationCountry the ISO 3166-1 Alpha-2 country code
     * @param code                 the company code
     * @return optional containing the corporation if found
     */
    Optional<CorporationEntity> findByIncorporationCountryAndCode(
            String incorporationCountry, String code);
}

