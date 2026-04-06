package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link BankAccountEntity}.
 */
@Repository
public interface BankAccountRepository
        extends JpaRepository<BankAccountEntity, UUID> {

    /**
     * Find a bank account by its IBAN (used for idempotent create-or-locate).
     *
     * @param iban the IBAN
     * @return optional containing the bank account if found
     */
    Optional<BankAccountEntity> findByIban(String iban);
}

