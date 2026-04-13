package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {
    Optional<BankAccountEntity> findByIban(String iban);
    List<BankAccountEntity> findByCurrency(String currency);
    List<BankAccountEntity> findByCountry(String country);
}
