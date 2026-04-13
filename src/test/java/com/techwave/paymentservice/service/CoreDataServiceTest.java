package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.dto.response.SiloResponse;
import com.techwave.paymentservice.entity.CountryEntity;
import com.techwave.paymentservice.entity.CurrencyEntity;
import com.techwave.paymentservice.entity.SiloEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CountryMapper;
import com.techwave.paymentservice.mapper.CurrencyMapper;
import com.techwave.paymentservice.mapper.SiloMapper;
import com.techwave.paymentservice.repository.CountryRepository;
import com.techwave.paymentservice.repository.CurrencyRepository;
import com.techwave.paymentservice.repository.SiloRepository;
import com.techwave.paymentservice.service.impl.CoreDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CoreDataService Tests")
class CoreDataServiceTest {
    
    @Mock
    private CountryRepository countryRepository;
    
    @Mock
    private CurrencyRepository currencyRepository;
    
    @Mock
    private SiloRepository siloRepository;
    
    @Mock
    private CountryMapper countryMapper;
    
    @Mock
    private CurrencyMapper currencyMapper;
    
    @Mock
    private SiloMapper siloMapper;
    
    @InjectMocks
    private CoreDataServiceImpl coreDataService;
    
    private CountryEntity countryEntity;
    private CountryResponse countryResponse;
    private CurrencyEntity currencyEntity;
    private CurrencyResponse currencyResponse;
    private SiloEntity siloEntity;
    private SiloResponse siloResponse;
    
    @BeforeEach
    void setUp() {
        countryEntity = new CountryEntity("US", "United States");
        countryResponse = new CountryResponse("US", "United States");
        
        currencyEntity = new CurrencyEntity("USD", "US Dollar");
        currencyResponse = new CurrencyResponse("USD", "US Dollar");
        
        siloEntity = new SiloEntity("silo1", "Silo 1");
        siloResponse = new SiloResponse("silo1", "Silo 1");
    }
    
    @Nested
    @DisplayName("getCountries() Tests")
    class GetCountriesTests {
        
        @Test
        @DisplayName("Should return all countries when available")
        void shouldReturnAllCountries() {
            // Given
            List<CountryEntity> entities = Arrays.asList(
                    countryEntity,
                    new CountryEntity("GB", "United Kingdom")
            );
            List<CountryResponse> responses = Arrays.asList(
                    countryResponse,
                    new CountryResponse("GB", "United Kingdom")
            );
            
            when(countryRepository.findAll()).thenReturn(entities);
            when(countryMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<CountryResponse> result = coreDataService.getCountries();
            
            // Then
            assertThat(result).hasSize(2).containsExactlyElementsOf(responses);
            verify(countryRepository).findAll();
        }
        
        @Test
        @DisplayName("Should return empty list when no countries exist")
        void shouldReturnEmptyList_NoCountries() {
            // Given
            when(countryRepository.findAll()).thenReturn(Collections.emptyList());
            when(countryMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<CountryResponse> result = coreDataService.getCountries();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getCountry() Tests")
    class GetCountryTests {
        
        @Test
        @DisplayName("Should return country when found")
        void shouldReturnCountry_Found() {
            // Given
            when(countryRepository.findById("US")).thenReturn(Optional.of(countryEntity));
            when(countryMapper.toResponse(countryEntity)).thenReturn(countryResponse);
            
            // When
            CountryResponse result = coreDataService.getCountry("US");
            
            // Then
            assertThat(result).isEqualTo(countryResponse);
            verify(countryRepository).findById("US");
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when country not found")
        void shouldThrowException_CountryNotFound() {
            // Given
            when(countryRepository.findById("XX")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCountry("XX"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Country not found");
        }
    }
    
    @Nested
    @DisplayName("getCurrencies() Tests")
    class GetCurrenciesTests {
        
        @Test
        @DisplayName("Should return all currencies when available")
        void shouldReturnAllCurrencies() {
            // Given
            List<CurrencyEntity> entities = Arrays.asList(
                    currencyEntity,
                    new CurrencyEntity("EUR", "Euro")
            );
            List<CurrencyResponse> responses = Arrays.asList(
                    currencyResponse,
                    new CurrencyResponse("EUR", "Euro")
            );
            
            when(currencyRepository.findAll()).thenReturn(entities);
            when(currencyMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<CurrencyResponse> result = coreDataService.getCurrencies();
            
            // Then
            assertThat(result).hasSize(2).containsExactlyElementsOf(responses);
        }
        
        @Test
        @DisplayName("Should return empty list when no currencies exist")
        void shouldReturnEmptyList_NoCurrencies() {
            // Given
            when(currencyRepository.findAll()).thenReturn(Collections.emptyList());
            when(currencyMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<CurrencyResponse> result = coreDataService.getCurrencies();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getCurrency() Tests")
    class GetCurrencyTests {
        
        @Test
        @DisplayName("Should return currency when found")
        void shouldReturnCurrency_Found() {
            // Given
            when(currencyRepository.findById("USD")).thenReturn(Optional.of(currencyEntity));
            when(currencyMapper.toResponse(currencyEntity)).thenReturn(currencyResponse);
            
            // When
            CurrencyResponse result = coreDataService.getCurrency("USD");
            
            // Then
            assertThat(result).isEqualTo(currencyResponse);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when currency not found")
        void shouldThrowException_CurrencyNotFound() {
            // Given
            when(currencyRepository.findById("XXX")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCurrency("XXX"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Currency not found");
        }
    }
    
    @Nested
    @DisplayName("getSilos() Tests")
    class GetSilosTests {
        
        @Test
        @DisplayName("Should return all silos when available")
        void shouldReturnAllSilos() {
            // Given
            List<SiloEntity> entities = Arrays.asList(
                    siloEntity,
                    new SiloEntity("silo2", "Silo 2")
            );
            List<SiloResponse> responses = Arrays.asList(
                    siloResponse,
                    new SiloResponse("silo2", "Silo 2")
            );
            
            when(siloRepository.findAll()).thenReturn(entities);
            when(siloMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<SiloResponse> result = coreDataService.getSilos();
            
            // Then
            assertThat(result).hasSize(2).containsExactlyElementsOf(responses);
        }
        
        @Test
        @DisplayName("Should return empty list when no silos exist")
        void shouldReturnEmptyList_NoSilos() {
            // Given
            when(siloRepository.findAll()).thenReturn(Collections.emptyList());
            when(siloMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<SiloResponse> result = coreDataService.getSilos();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getSilo() Tests")
    class GetSiloTests {
        
        @Test
        @DisplayName("Should return silo when found")
        void shouldReturnSilo_Found() {
            // Given
            when(siloRepository.findById("silo1")).thenReturn(Optional.of(siloEntity));
            when(siloMapper.toResponse(siloEntity)).thenReturn(siloResponse);
            
            // When
            SiloResponse result = coreDataService.getSilo("silo1");
            
            // Then
            assertThat(result).isEqualTo(siloResponse);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when silo not found")
        void shouldThrowException_SiloNotFound() {
            // Given
            when(siloRepository.findById("invalid")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getSilo("invalid"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Silo not found");
        }
    }
}
package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.dto.response.SiloResponse;
import com.techwave.paymentservice.entity.CountryEntity;
import com.techwave.paymentservice.entity.CurrencyEntity;
import com.techwave.paymentservice.entity.SiloEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CountryMapper;
import com.techwave.paymentservice.mapper.CurrencyMapper;
import com.techwave.paymentservice.mapper.SiloMapper;
import com.techwave.paymentservice.repository.CountryRepository;
import com.techwave.paymentservice.repository.CurrencyRepository;
import com.techwave.paymentservice.repository.SiloRepository;
import com.techwave.paymentservice.service.impl.CoreDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CoreDataService Tests")
class CoreDataServiceTest {
    
    @Mock
    private CountryRepository countryRepository;
    
    @Mock
    private CurrencyRepository currencyRepository;
    
    @Mock
    private SiloRepository siloRepository;
    
    @Mock
    private CountryMapper countryMapper;
    
    @Mock
    private CurrencyMapper currencyMapper;
    
    @Mock
    private SiloMapper siloMapper;
    
    @InjectMocks
    private CoreDataServiceImpl coreDataService;
    
    private CountryEntity countryEntity;
    private CountryResponse countryResponse;
    private CurrencyEntity currencyEntity;
    private CurrencyResponse currencyResponse;
    private SiloEntity siloEntity;
    private SiloResponse siloResponse;
    
    @BeforeEach
    void setUp() {
        countryEntity = new CountryEntity("US", "United States");
        countryResponse = new CountryResponse("US", "United States");
        
        currencyEntity = new CurrencyEntity("USD", "US Dollar");
        currencyResponse = new CurrencyResponse("USD", "US Dollar");
        
        siloEntity = new SiloEntity("silo1", "Silo 1");
        siloResponse = new SiloResponse("silo1", "Silo 1");
    }
    
    @Nested
    @DisplayName("getCountries() Tests")
    class GetCountriesTests {
        
        @Test
        @DisplayName("Should return all countries when available")
        void shouldReturnAllCountries() {
            // Given
            List<CountryEntity> entities = Arrays.asList(
                    countryEntity,
                    new CountryEntity("GB", "United Kingdom"),
                    new CountryEntity("DE", "Germany")
            );
            List<CountryResponse> responses = Arrays.asList(
                    countryResponse,
                    new CountryResponse("GB", "United Kingdom"),
                    new CountryResponse("DE", "Germany")
            );
            
            when(countryRepository.findAll()).thenReturn(entities);
            when(countryMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<CountryResponse> result = coreDataService.getCountries();
            
            // Then
            assertThat(result).hasSize(3)
                    .containsExactlyElementsOf(responses);
            verify(countryRepository, times(1)).findAll();
            verify(countryMapper, times(1)).toResponses(entities);
        }
        
        @Test
        @DisplayName("Should return empty list when no countries exist")
        void shouldReturnEmptyListWhenNoCountries() {
            // Given
            when(countryRepository.findAll()).thenReturn(Collections.emptyList());
            when(countryMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<CountryResponse> result = coreDataService.getCountries();
            
            // Then
            assertThat(result).isEmpty();
            verify(countryRepository).findAll();
            verify(countryMapper).toResponses(Collections.emptyList());
        }
    }
    
    @Nested
    @DisplayName("getCountry(String) Tests")
    class GetCountryTests {
        
        @Test
        @DisplayName("Should return country when found")
        void shouldReturnCountry_Found() {
            // Given
            when(countryRepository.findById("US")).thenReturn(Optional.of(countryEntity));
            when(countryMapper.toResponse(countryEntity)).thenReturn(countryResponse);
            
            // When
            CountryResponse result = coreDataService.getCountry("US");
            
            // Then
            assertThat(result).isEqualTo(countryResponse);
            assertThat(result.getId()).isEqualTo("US");
            assertThat(result.getName()).isEqualTo("United States");
            verify(countryRepository).findById("US");
            verify(countryMapper).toResponse(countryEntity);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when country not found")
        void shouldThrowException_CountryNotFound() {
            // Given
            when(countryRepository.findById("XX")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCountry("XX"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Country not found: XX");
            
            verify(countryRepository).findById("XX");
            verify(countryMapper, never()).toResponse(any());
        }
        
        @Test
        @DisplayName("Should handle case-sensitive country code lookup")
        void shouldHandleCaseSensitiveCountryCodes() {
            // Given
            when(countryRepository.findById("us")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCountry("us"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Country not found");
        }
    }
    
    @Nested
    @DisplayName("getCurrencies() Tests")
    class GetCurrenciesTests {
        
        @Test
        @DisplayName("Should return all currencies when available")
        void shouldReturnAllCurrencies() {
            // Given
            List<CurrencyEntity> entities = Arrays.asList(
                    currencyEntity,
                    new CurrencyEntity("EUR", "Euro"),
                    new CurrencyEntity("GBP", "British Pound")
            );
            List<CurrencyResponse> responses = Arrays.asList(
                    currencyResponse,
                    new CurrencyResponse("EUR", "Euro"),
                    new CurrencyResponse("GBP", "British Pound")
            );
            
            when(currencyRepository.findAll()).thenReturn(entities);
            when(currencyMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<CurrencyResponse> result = coreDataService.getCurrencies();
            
            // Then
            assertThat(result).hasSize(3)
                    .containsExactlyElementsOf(responses);
            verify(currencyRepository).findAll();
            verify(currencyMapper).toResponses(entities);
        }
        
        @Test
        @DisplayName("Should return empty list when no currencies exist")
        void shouldReturnEmptyList_NoCurrencies() {
            // Given
            when(currencyRepository.findAll()).thenReturn(Collections.emptyList());
            when(currencyMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<CurrencyResponse> result = coreDataService.getCurrencies();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getCurrency(String) Tests")
    class GetCurrencyTests {
        
        @Test
        @DisplayName("Should return currency when found")
        void shouldReturnCurrency_Found() {
            // Given
            when(currencyRepository.findById("USD")).thenReturn(Optional.of(currencyEntity));
            when(currencyMapper.toResponse(currencyEntity)).thenReturn(currencyResponse);
            
            // When
            CurrencyResponse result = coreDataService.getCurrency("USD");
            
            // Then
            assertThat(result).isEqualTo(currencyResponse);
            assertThat(result.getId()).isEqualTo("USD");
            assertThat(result.getName()).isEqualTo("US Dollar");
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when currency not found")
        void shouldThrowException_CurrencyNotFound() {
            // Given
            when(currencyRepository.findById("XYZ")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCurrency("XYZ"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Currency not found: XYZ");
        }
        
        @Test
        @DisplayName("Should handle multiple currency lookups independently")
        void shouldHandleMultipleCurrencyLookups() {
            // Given
            CurrencyEntity eurEntity = new CurrencyEntity("EUR", "Euro");
            CurrencyResponse eurResponse = new CurrencyResponse("EUR", "Euro");
            
            when(currencyRepository.findById("USD")).thenReturn(Optional.of(currencyEntity));
            when(currencyRepository.findById("EUR")).thenReturn(Optional.of(eurEntity));
            when(currencyMapper.toResponse(currencyEntity)).thenReturn(currencyResponse);
            when(currencyMapper.toResponse(eurEntity)).thenReturn(eurResponse);
            
            // When
            CurrencyResponse usdResult = coreDataService.getCurrency("USD");
            CurrencyResponse eurResult = coreDataService.getCurrency("EUR");
            
            // Then
            assertThat(usdResult.getId()).isEqualTo("USD");
            assertThat(eurResult.getId()).isEqualTo("EUR");
        }
    }
    
    @Nested
    @DisplayName("getSilos() Tests")
    class GetSilosTests {
        
        @Test
        @DisplayName("Should return all silos when available")
        void shouldReturnAllSilos() {
            // Given
            List<SiloEntity> entities = Arrays.asList(
                    siloEntity,
                    new SiloEntity("silo2", "Silo 2"),
                    new SiloEntity("silo3", "Silo 3")
            );
            List<SiloResponse> responses = Arrays.asList(
                    siloResponse,
                    new SiloResponse("silo2", "Silo 2"),
                    new SiloResponse("silo3", "Silo 3")
            );
            
            when(siloRepository.findAll()).thenReturn(entities);
            when(siloMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<SiloResponse> result = coreDataService.getSilos();
            
            // Then
            assertThat(result).hasSize(3)
                    .containsExactlyElementsOf(responses);
            verify(siloRepository).findAll();
        }
        
        @Test
        @DisplayName("Should return empty list when no silos exist")
        void shouldReturnEmptyList_NoSilos() {
            // Given
            when(siloRepository.findAll()).thenReturn(Collections.emptyList());
            when(siloMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<SiloResponse> result = coreDataService.getSilos();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getSilo(String) Tests")
    class GetSiloTests {
        
        @Test
        @DisplayName("Should return silo when found")
        void shouldReturnSilo_Found() {
            // Given
            when(siloRepository.findById("silo1")).thenReturn(Optional.of(siloEntity));
            when(siloMapper.toResponse(siloEntity)).thenReturn(siloResponse);
            
            // When
            SiloResponse result = coreDataService.getSilo("silo1");
            
            // Then
            assertThat(result).isEqualTo(siloResponse);
            assertThat(result.getId()).isEqualTo("silo1");
            assertThat(result.getName()).isEqualTo("Silo 1");
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when silo not found")
        void shouldThrowException_SiloNotFound() {
            // Given
            when(siloRepository.findById("invalid")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getSilo("invalid"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Silo not found: invalid");
        }
    }
}

            
            // Then
            assertThat(result).hasSize(2)
                    .containsExactlyElementsOf(responses);
            verify(countryRepository, times(1)).findAll();
            verify(countryMapper, times(1)).toResponses(entities);
        }
        
        @Test
        @DisplayName("Should return empty list when no countries exist")
        void shouldReturnEmptyListWhenNoCountries() {
            // Given
            when(countryRepository.findAll()).thenReturn(Collections.emptyList());
            when(countryMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<CountryResponse> result = coreDataService.getCountries();
            
            // Then
            assertThat(result).isEmpty();
            verify(countryRepository).findAll();
        }
    }
    
    @Nested
    @DisplayName("getCountry(String) Tests")
    class GetCountryTests {
        
        @Test
        @DisplayName("Should return country when found")
        void shouldReturnCountry_Found() {
            // Given
            when(countryRepository.findById("US")).thenReturn(Optional.of(countryEntity));
            when(countryMapper.toResponse(countryEntity)).thenReturn(countryResponse);
            
            // When
            CountryResponse result = coreDataService.getCountry("US");
            
            // Then
            assertThat(result).isEqualTo(countryResponse);
            verify(countryRepository).findById("US");
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when country not found")
        void shouldThrowException_CountryNotFound() {
            // Given
            when(countryRepository.findById("XX")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCountry("XX"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Country not found");
            
            verify(countryRepository).findById("XX");
            verify(countryMapper, never()).toResponse(any());
        }
        
        @Test
        @DisplayName("Should handle case-sensitive country code lookup")
        void shouldHandleCaseSensitiveCountryCodes() {
            // Given
            when(countryRepository.findById("us")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCountry("us"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
    
    @Nested
    @DisplayName("getCurrencies() Tests")
    class GetCurrenciesTests {
        
        @Test
        @DisplayName("Should return all currencies when available")
        void shouldReturnAllCurrencies() {
            // Given
            List<CurrencyEntity> entities = Arrays.asList(
                    currencyEntity,
                    new CurrencyEntity("EUR", "Euro")
            );
            List<CurrencyResponse> responses = Arrays.asList(
                    currencyResponse,
                    new CurrencyResponse("EUR", "Euro")
            );
            
            when(currencyRepository.findAll()).thenReturn(entities);
            when(currencyMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<CurrencyResponse> result = coreDataService.getCurrencies();
            
            // Then
            assertThat(result).hasSize(2)
                    .containsExactlyElementsOf(responses);
            verify(currencyRepository).findAll();
            verify(currencyMapper).toResponses(entities);
        }
        
        @Test
        @DisplayName("Should return empty list when no currencies exist")
        void shouldReturnEmptyListWhenNoCurrencies() {
            // Given
            when(currencyRepository.findAll()).thenReturn(Collections.emptyList());
            when(currencyMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<CurrencyResponse> result = coreDataService.getCurrencies();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getCurrency(String) Tests")
    class GetCurrencyTests {
        
        @Test
        @DisplayName("Should return currency when found")
        void shouldReturnCurrency_Found() {
            // Given
            when(currencyRepository.findById("USD")).thenReturn(Optional.of(currencyEntity));
            when(currencyMapper.toResponse(currencyEntity)).thenReturn(currencyResponse);
            
            // When
            CurrencyResponse result = coreDataService.getCurrency("USD");
            
            // Then
            assertThat(result).isEqualTo(currencyResponse);
            verify(currencyRepository).findById("USD");
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when currency not found")
        void shouldThrowException_CurrencyNotFound() {
            // Given
            when(currencyRepository.findById("XXX")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getCurrency("XXX"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Currency not found");
        }
    }
    
    @Nested
    @DisplayName("getSilos() Tests")
    class GetSilosTests {
        
        @Test
        @DisplayName("Should return all silos when available")
        void shouldReturnAllSilos() {
            // Given
            List<SiloEntity> entities = Arrays.asList(
                    siloEntity,
                    new SiloEntity("silo2", "Silo 2")
            );
            List<SiloResponse> responses = Arrays.asList(
                    siloResponse,
                    new SiloResponse("silo2", "Silo 2")
            );
            
            when(siloRepository.findAll()).thenReturn(entities);
            when(siloMapper.toResponses(entities)).thenReturn(responses);
            
            // When
            List<SiloResponse> result = coreDataService.getSilos();
            
            // Then
            assertThat(result).hasSize(2)
                    .containsExactlyElementsOf(responses);
            verify(siloRepository).findAll();
        }
        
        @Test
        @DisplayName("Should return empty list when no silos exist")
        void shouldReturnEmptyListWhenNoSilos() {
            // Given
            when(siloRepository.findAll()).thenReturn(Collections.emptyList());
            when(siloMapper.toResponses(Collections.emptyList())).thenReturn(Collections.emptyList());
            
            // When
            List<SiloResponse> result = coreDataService.getSilos();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getSilo(String) Tests")
    class GetSiloTests {
        
        @Test
        @DisplayName("Should return silo when found")
        void shouldReturnSilo_Found() {
            // Given
            when(siloRepository.findById("silo1")).thenReturn(Optional.of(siloEntity));
            when(siloMapper.toResponse(siloEntity)).thenReturn(siloResponse);
            
            // When
            SiloResponse result = coreDataService.getSilo("silo1");
            
            // Then
            assertThat(result).isEqualTo(siloResponse);
            verify(siloRepository).findById("silo1");
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when silo not found")
        void shouldThrowException_SiloNotFound() {
            // Given
            when(siloRepository.findById("invalid")).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> coreDataService.getSilo("invalid"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Silo not found");
        }
    }
}
