package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CorporationAuditDto;
import com.techwave.paymentservice.dto.CorporationDto;
import com.techwave.paymentservice.entity.CorporationAuditEntity;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CorporationMapper;
import com.techwave.paymentservice.repository.CorporationAuditRepository;
import com.techwave.paymentservice.repository.CorporationRepository;
import com.techwave.paymentservice.service.impl.CorporationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CorporationService Unit Tests")
class CorporationServiceTest {

    @Mock
    private CorporationRepository corporationRepository;

    @Mock
    private CorporationAuditRepository corporationAuditRepository;

    @Mock
    private CorporationMapper corporationMapper;

    @InjectMocks
    private CorporationServiceImpl corporationService;

    private UUID corpId;
    private CorporationEntity entity;
    private CorporationDto dto;

    @BeforeEach
    void setUp() {
        corpId = UUID.randomUUID();

        entity = new CorporationEntity();
        entity.setId(corpId);
        entity.setName("Acme Corp");
        entity.setCode("ACME");
        entity.setIncorporationCountry("US");
        entity.setIncorporationDate(LocalDate.of(2020, 1, 15));
        entity.setVersion(1);

        dto = new CorporationDto();
        dto.setId(corpId);
        dto.setName("Acme Corp");
        dto.setCode("ACME");
        dto.setIncorporationCountry("US");
        dto.setIncorporationDate(LocalDate.of(2020, 1, 15));
    }

    @Test
    @DisplayName("createCorporation saves and creates audit")
    void createCorporation_success() {
        CorporationEntity newEntity = new CorporationEntity();
        when(corporationMapper.toEntity(dto)).thenReturn(newEntity);
        when(corporationRepository.save(any(CorporationEntity.class)))
                .thenReturn(entity);
        when(corporationMapper.toDto(entity)).thenReturn(dto);

        CorporationDto result =
                corporationService.createCorporation(dto);

        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
        verify(corporationRepository).save(any());
        verify(corporationAuditRepository).save(any());
    }

    @Test
    @DisplayName("getCorporationById returns DTO when found")
    void getCorporationById_found() {
        when(corporationRepository.findById(corpId))
                .thenReturn(Optional.of(entity));
        when(corporationMapper.toDto(entity)).thenReturn(dto);

        CorporationDto result =
                corporationService.getCorporationById(corpId);

        assertNotNull(result);
        assertEquals(corpId, result.getId());
    }

    @Test
    @DisplayName("getCorporationById throws when not found")
    void getCorporationById_notFound() {
        UUID id = UUID.randomUUID();
        when(corporationRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> corporationService.getCorporationById(id));
    }

    @Test
    @DisplayName("getCorporationByCode returns DTO when found")
    void getCorporationByCode_found() {
        when(corporationRepository
                .findByIncorporationCountryAndCode("US", "ACME"))
                .thenReturn(Optional.of(entity));
        when(corporationMapper.toDto(entity)).thenReturn(dto);

        CorporationDto result =
                corporationService.getCorporationByCode("US", "ACME");

        assertNotNull(result);
        assertEquals("ACME", result.getCode());
    }

    @Test
    @DisplayName("getCorporationByCode throws when not found")
    void getCorporationByCode_notFound() {
        when(corporationRepository
                .findByIncorporationCountryAndCode("XX", "NONE"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> corporationService
                        .getCorporationByCode("XX", "NONE"));
    }

    @Test
    @DisplayName("updateCorporation increments version")
    void updateCorporation_success() {
        CorporationDto updateDto = new CorporationDto();
        updateDto.setName("Acme Updated");

        CorporationEntity updatedEntity = new CorporationEntity();
        updatedEntity.setId(corpId);
        updatedEntity.setName("Acme Updated");
        updatedEntity.setVersion(2);

        CorporationDto updatedDto = new CorporationDto();
        updatedDto.setId(corpId);
        updatedDto.setName("Acme Updated");

        when(corporationRepository.findById(corpId))
                .thenReturn(Optional.of(entity));
        when(corporationRepository.saveAndFlush(entity))
                .thenReturn(updatedEntity);
        when(corporationMapper.toDto(updatedEntity))
                .thenReturn(updatedDto);

        CorporationDto result =
                corporationService.updateCorporation(corpId, updateDto);

        assertEquals("Acme Updated", result.getName());
        verify(corporationAuditRepository).save(any());
    }

    @Test
    @DisplayName("getAuditTrail returns ordered entries")
    void getAuditTrail_success() {
        CorporationAuditEntity auditEntity =
                new CorporationAuditEntity();
        auditEntity.setResource(corpId);
        auditEntity.setVersion(1);

        CorporationAuditDto auditDto = new CorporationAuditDto();
        auditDto.setResource(corpId);
        auditDto.setVersion(1);

        when(corporationRepository.existsById(corpId)).thenReturn(true);
        when(corporationAuditRepository
                .findByResourceOrderByVersionAsc(corpId))
                .thenReturn(List.of(auditEntity));
        when(corporationMapper.toAuditDtoList(List.of(auditEntity)))
                .thenReturn(List.of(auditDto));

        List<CorporationAuditDto> result =
                corporationService.getAuditTrail(corpId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getVersion());
    }
}

