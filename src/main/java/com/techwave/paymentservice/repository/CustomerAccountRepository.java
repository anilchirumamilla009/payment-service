package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.entity.CustomerAccountState;
import com.techwave.paymentservice.entity.CustomerAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccountEntity, UUID> {
    List<CustomerAccountEntity> findBySilo(String silo);
    List<CustomerAccountEntity> findByAccountType(CustomerAccountType type);
    List<CustomerAccountEntity> findByAccountState(CustomerAccountState state);
}
