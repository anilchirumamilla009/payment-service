package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.CorporationAuditDto;
import com.techwave.paymentservice.dto.CorporationDto;
import com.techwave.paymentservice.entity.CorporationAuditEntity;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CorporationMapper;
import com.techwave.paymentservice.repository.CorporationAuditRepository;
import com.techwave.paymentservice.repository.CorporationRepository;
import com.techwave.paymentservice.service.CorporationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link CorporationService}.
 * Manages corporation lifecycle including creation, retrieval,
 * update, lookup by code, and audit trail tracking.
 */
@Service
@Transactional
public class CorporationServiceImpl implements CorporationService {

    private static final Logger log =
            LoggerFactory.getLogger(CorporationServiceImpl.class);

    private final CorporationRepository corporationRepository;
    private final CorporationAuditRepository corporationAuditRepository;
    private final CorporationMapper corporationMapper;

    public CorporationServiceImpl(
            CorporationRepository corporationRepository,
            CorporationAuditRepository corporationAuditRepository,
            CorporationMapper corporationMapper) {
        this.corporationRepository = corporationRepository;
        this.corporationAuditRepository = corporationAuditRepository;
        this.corporationMapper = corporationMapper;
    }

    @Override
    public CorporationDto createCorporation(CorporationDto dto) {
        log.debug("Creating corporation: {}", dto.getName());
        CorporationEntity entity = corporationMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setVersion(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CorporationEntity saved = corporationRepository.save(entity);
        createAuditRecord(saved);

        log.info("Corporation created with id: {}", saved.getId());
        return corporationMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CorporationDto getCorporationById(UUID uuid) {
        log.debug("Fetching corporation with id: {}", uuid);
        return corporationRepository.findById(uuid)
                .map(corporationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Corporation", uuid.toString()));
    }

    @Override
    public CorporationDto updateCorporation(UUID uuid,
                                            CorporationDto dto) {
        log.debug("Updating corporation with id: {}", uuid);
        CorporationEntity entity = corporationRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Corporation", uuid.toString()));

        corporationMapper.updateEntity(dto, entity);
        entity.setVersion(entity.getVersion() + 1);
        entity.setUpdatedAt(LocalDateTime.now());

        CorporationEntity saved = corporationRepository.save(entity);
        createAuditRecord(saved);

        log.info("Corporation updated with id: {}, version: {}",
                saved.getId(), saved.getVersion());
        return corporationMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CorporationAuditDto> getAuditTrail(UUID uuid) {
        log.debug("Fetching audit trail for corporation: {}", uuid);
        if (!corporationRepository.existsById(uuid)) {
            throw new ResourceNotFoundException(
                    "Corporation", uuid.toString());
        }
        List<CorporationAuditEntity> audits =
                corporationAuditRepository
                        .findByResourceOrderByVersionAsc(uuid);
        return corporationMapper.toAuditDtoList(audits);
    }

    @Override
    @Transactional(readOnly = true)
    public CorporationDto getCorporationByCode(String country,
                                               String code) {
        log.debug("Fetching corporation by country: {}, code: {}",
                country, code);
        return corporationRepository
                .findByIncorporationCountryAndCode(country, code)
                .map(corporationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Corporation",
                        "country=" + country + ", code=" + code));
    }

    private void createAuditRecord(CorporationEntity entity) {
        CorporationAuditEntity audit = new CorporationAuditEntity();
        audit.setResource(entity.getId());
        audit.setVersion(entity.getVersion());
        audit.setName(entity.getName());
        audit.setCode(entity.getCode());
        audit.setIncorporationDate(entity.getIncorporationDate());
        audit.setIncorporationCountry(entity.getIncorporationCountry());
        audit.setType(entity.getType());
        audit.setDuplicates(entity.getDuplicates());
        audit.setCreatedAt(LocalDateTime.now());
        corporationAuditRepository.save(audit);
        log.debug("Audit record created for corporation: {}, version: {}",
                entity.getId(), entity.getVersion());
    }
}

