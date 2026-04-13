package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.SiloEntity;
import com.techwave.paymentservice.entity.SiloType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SiloRepository extends JpaRepository<SiloEntity, String> {
    List<SiloEntity> findByType(SiloType type);
}
