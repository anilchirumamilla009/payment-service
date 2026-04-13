package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, String> {
    List<CountryEntity> findByEurozone(boolean eurozone);
    List<CountryEntity> findBySepa(boolean sepa);
}
