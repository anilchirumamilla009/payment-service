package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CurrencyDto;
import com.techwave.paymentservice.entity.CurrencyEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CurrencyMapper;
import com.techwave.paymentservice.repository.CurrencyRepository;
import com.techwave.paymentservice.service.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CurrencyService Unit Tests")
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyMapper currencyMapper;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private CurrencyEntity entity;
    private CurrencyDto dto;

    @BeforeEach
    void setUp() {
        entity = new CurrencyEntity("EUR", "Euro");

        dto = new CurrencyDto();
        dto.setId("EUR");
        dto.setName("Euro");
    }

    @Test
    @DisplayName("getAllCurrencies returns list of DTOs")
    void getAllCurrencies_returnsList() {
        when(currencyRepository.findAll()).thenReturn(List.of(entity));
        when(currencyMapper.toDtoList(List.of(entity)))
                .thenReturn(List.of(dto));

        List<CurrencyDto> result = currencyService.getAllCurrencies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EUR", result.get(0).getId());
        verify(currencyRepository).findAll();
    }

    @Test
    @DisplayName("getAllCurrencies returns empty list when no currencies")
    void getAllCurrencies_returnsEmptyList() {
        when(currencyRepository.findAll()).thenReturn(List.of());
        when(currencyMapper.toDtoList(List.of())).thenReturn(List.of());

        List<CurrencyDto> result = currencyService.getAllCurrencies();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getCurrencyById returns DTO when found")
    void getCurrencyById_found() {
        when(currencyRepository.findById("EUR"))
                .thenReturn(Optional.of(entity));
        when(currencyMapper.toDto(entity)).thenReturn(dto);

        CurrencyDto result = currencyService.getCurrencyById("EUR");

        assertNotNull(result);
        assertEquals("EUR", result.getId());
        assertEquals("Euro", result.getName());
        verify(currencyRepository).findById("EUR");
    }

    @Test
    @DisplayName("getCurrencyById throws when not found")
    void getCurrencyById_notFound() {
        when(currencyRepository.findById("XXX"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> currencyService.getCurrencyById("XXX"));
    }
}

