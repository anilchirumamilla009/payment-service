package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CountryDto;
import com.techwave.paymentservice.entity.CountryEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CountryMapper;
import com.techwave.paymentservice.repository.CountryRepository;
import com.techwave.paymentservice.service.impl.CountryServiceImpl;
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
@DisplayName("CountryService Unit Tests")
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    private CountryEntity entity;
    private CountryDto dto;

    @BeforeEach
    void setUp() {
        entity = new CountryEntity("US", "United States of America",
                "840", "USA", false, false);

        dto = new CountryDto();
        dto.setId("US");
        dto.setName("United States of America");
        dto.setNumericCode("840");
        dto.setAlpha3Code("USA");
        dto.setEurozone(false);
        dto.setSepa(false);
    }

    @Test
    @DisplayName("getAllCountries returns list of DTOs")
    void getAllCountries_returnsCountryList() {
        when(countryRepository.findAll())
                .thenReturn(List.of(entity));
        when(countryMapper.toDtoList(List.of(entity)))
                .thenReturn(List.of(dto));

        List<CountryDto> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("US", result.get(0).getId());
        verify(countryRepository).findAll();
    }

    @Test
    @DisplayName("getCountryById returns DTO when found")
    void getCountryById_found_returnsDto() {
        when(countryRepository.findById("US"))
                .thenReturn(Optional.of(entity));
        when(countryMapper.toDto(entity)).thenReturn(dto);

        CountryDto result = countryService.getCountryById("US");

        assertNotNull(result);
        assertEquals("US", result.getId());
        verify(countryRepository).findById("US");
    }

    @Test
    @DisplayName("getCountryById throws when not found")
    void getCountryById_notFound_throwsException() {
        when(countryRepository.findById("XX"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> countryService.getCountryById("XX"));
    }
}

