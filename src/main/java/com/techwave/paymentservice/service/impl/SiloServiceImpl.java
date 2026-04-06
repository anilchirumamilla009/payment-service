package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.SiloDto;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.SiloMapper;
import com.techwave.paymentservice.repository.SiloRepository;
import com.techwave.paymentservice.service.SiloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementation of {@link SiloService}.
 * Provides read-only operations for silo reference data.
 */
@Service
@Transactional(readOnly = true)
public class SiloServiceImpl implements SiloService {

    private static final Logger log =
            LoggerFactory.getLogger(SiloServiceImpl.class);

    private final SiloRepository siloRepository;
    private final SiloMapper siloMapper;

    public SiloServiceImpl(SiloRepository siloRepository,
                           SiloMapper siloMapper) {
        this.siloRepository = siloRepository;
        this.siloMapper = siloMapper;
    }

    @Override
    public List<SiloDto> getAllSilos() {
        log.debug("Fetching all silos");
        return siloMapper.toDtoList(siloRepository.findAll());
    }

    @Override
    public SiloDto getSiloById(String id) {
        log.debug("Fetching silo with id: {}", id);
        return siloRepository.findById(id)
                .map(siloMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Silo", id));
    }
}

