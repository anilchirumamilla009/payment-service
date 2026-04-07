package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.SiloDto;
import com.techwave.paymentservice.entity.SiloEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.SiloMapper;
import com.techwave.paymentservice.repository.SiloRepository;
import com.techwave.paymentservice.service.impl.SiloServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SiloService Unit Tests")
class SiloServiceTest {

    @Mock
    private SiloRepository siloRepository;

    @Mock
    private SiloMapper siloMapper;

    @InjectMocks
    private SiloServiceImpl siloService;

    private SiloEntity entity;
    private SiloDto dto;

    @BeforeEach
    void setUp() {
        entity = new SiloEntity();
        entity.setId("TREASURY");
        entity.setName("Treasury");
        entity.setDescription("Main treasury silo");
        entity.setEmail("treasury@techwave.com");
        entity.setDefaultBaseCurrency("USD");
        entity.setDefaultCreditLimit(new BigDecimal("1000000.0000"));
        entity.setDefaultProfitShare(new BigDecimal("0.1500"));

        dto = new SiloDto();
        dto.setId("TREASURY");
        dto.setName("Treasury");
        dto.setDescription("Main treasury silo");
        dto.setEmail("treasury@techwave.com");
        dto.setDefaultBaseCurrency("USD");
        dto.setDefaultCreditLimit(new BigDecimal("1000000.0000"));
        dto.setDefaultProfitShare(new BigDecimal("0.1500"));
    }

    @Test
    @DisplayName("getAllSilos returns list of DTOs")
    void getAllSilos_returnsList() {
        when(siloRepository.findAll()).thenReturn(List.of(entity));
        when(siloMapper.toDtoList(List.of(entity)))
                .thenReturn(List.of(dto));

        List<SiloDto> result = siloService.getAllSilos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TREASURY", result.get(0).getId());
        verify(siloRepository).findAll();
    }

    @Test
    @DisplayName("getAllSilos returns empty list when none exist")
    void getAllSilos_returnsEmptyList() {
        when(siloRepository.findAll()).thenReturn(List.of());
        when(siloMapper.toDtoList(List.of())).thenReturn(List.of());

        List<SiloDto> result = siloService.getAllSilos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getSiloById returns DTO when found")
    void getSiloById_found() {
        when(siloRepository.findById("TREASURY"))
                .thenReturn(Optional.of(entity));
        when(siloMapper.toDto(entity)).thenReturn(dto);

        SiloDto result = siloService.getSiloById("TREASURY");

        assertNotNull(result);
        assertEquals("TREASURY", result.getId());
        assertEquals("Treasury", result.getName());
        verify(siloRepository).findById("TREASURY");
    }

    @Test
    @DisplayName("getSiloById throws when not found")
    void getSiloById_notFound() {
        when(siloRepository.findById("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> siloService.getSiloById("UNKNOWN"));
    }
}

