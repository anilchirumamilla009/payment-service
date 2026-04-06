package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.SiloDto;
import java.util.List;

/**
 * Service interface for silo operations.
 */
public interface SiloService {

    List<SiloDto> getAllSilos();

    SiloDto getSiloById(String id);
}

