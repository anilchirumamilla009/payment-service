package com.techwave.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.dto.response.SiloResponse;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.service.CoreDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoreDataController.class)
@DisplayName("CoreDataController Tests")
class CoreDataControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private CoreDataService coreDataService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Nested
    @DisplayName("getCountries() Tests")
    class GetCountriesTests {
        
        @Test
        @DisplayName("GET /api/v1/countries - Success")
        void getCountries_Success() throws Exception {
            // Given
            List<CountryResponse> countries = Arrays.asList(
                    new CountryResponse("US", "United States"),
                    new CountryResponse("GB", "United Kingdom")
            );
            when(coreDataService.getCountries()).thenReturn(countries);
            
            // When & Then
            mockMvc.perform(get("/api/v1/countries"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value("US"))
                    .andExpect(jsonPath("$[0].name").value("United States"))
                    .andExpect(jsonPath("$[1].id").value("GB"));
            
            verify(coreDataService, times(1)).getCountries();
        }
        
        @Test
        @DisplayName("GET /api/v1/countries - Empty List")
        void getCountries_EmptyList() throws Exception {
            // Given
            when(coreDataService.getCountries()).thenReturn(Collections.emptyList());
            
            // When & Then
            mockMvc.perform(get("/api/v1/countries"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
    
    @Nested
    @DisplayName("getCountry() Tests")
    class GetCountryTests {
        
        @Test
        @DisplayName("GET /api/v1/countries/{id} - Found")
        void getCountry_Found() throws Exception {
            // Given
            CountryResponse country = new CountryResponse("US", "United States");
            when(coreDataService.getCountry("US")).thenReturn(country);
            
            // When & Then
            mockMvc.perform(get("/api/v1/countries/US"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("US"))
                    .andExpect(jsonPath("$.name").value("United States"));
            
            verify(coreDataService).getCountry("US");
        }
        
        @Test
        @DisplayName("GET /api/v1/countries/{id} - Not Found")
        void getCountry_NotFound() throws Exception {
            // Given
            when(coreDataService.getCountry("XX"))
                    .thenThrow(new ResourceNotFoundException("Country not found: XX"));
            
            // When & Then
            mockMvc.perform(get("/api/v1/countries/XX"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("Country not found: XX"));
            
            verify(coreDataService).getCountry("XX");
        }
    }
    
    @Nested
    @DisplayName("getCurrencies() Tests")
    class GetCurrenciesTests {
        
        @Test
        @DisplayName("GET /api/v1/currencies - Success")
        void getCurrencies_Success() throws Exception {
            // Given
            List<CurrencyResponse> currencies = Arrays.asList(
                    new CurrencyResponse("USD", "US Dollar"),
                    new CurrencyResponse("EUR", "Euro")
            );
            when(coreDataService.getCurrencies()).thenReturn(currencies);
            
            // When & Then
            mockMvc.perform(get("/api/v1/currencies"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value("USD"))
                    .andExpect(jsonPath("$[1].id").value("EUR"));
            
            verify(coreDataService).getCurrencies();
        }
        
        @Test
        @DisplayName("GET /api/v1/currencies - Empty List")
        void getCurrencies_EmptyList() throws Exception {
            // Given
            when(coreDataService.getCurrencies()).thenReturn(Collections.emptyList());
            
            // When & Then
            mockMvc.perform(get("/api/v1/currencies"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
    
    @Nested
    @DisplayName("getCurrency() Tests")
    class GetCurrencyTests {
        
        @Test
        @DisplayName("GET /api/v1/currencies/{id} - Found")
        void getCurrency_Found() throws Exception {
            // Given
            CurrencyResponse currency = new CurrencyResponse("USD", "US Dollar");
            when(coreDataService.getCurrency("USD")).thenReturn(currency);
            
            // When & Then
            mockMvc.perform(get("/api/v1/currencies/USD"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("USD"))
                    .andExpect(jsonPath("$.name").value("US Dollar"));
            
            verify(coreDataService).getCurrency("USD");
        }
        
        @Test
        @DisplayName("GET /api/v1/currencies/{id} - Not Found")
        void getCurrency_NotFound() throws Exception {
            // Given
            when(coreDataService.getCurrency("XXX"))
                    .thenThrow(new ResourceNotFoundException("Currency not found: XXX"));
            
            // When & Then
            mockMvc.perform(get("/api/v1/currencies/XXX"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"));
        }
    }
    
    @Nested
    @DisplayName("getSilos() Tests")
    class GetSilosTests {
        
        @Test
        @DisplayName("GET /api/v1/silos - Success")
        void getSilos_Success() throws Exception {
            // Given
            List<SiloResponse> silos = Arrays.asList(
                    new SiloResponse("silo1", "Silo 1"),
                    new SiloResponse("silo2", "Silo 2")
            );
            when(coreDataService.getSilos()).thenReturn(silos);
            
            // When & Then
            mockMvc.perform(get("/api/v1/silos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value("silo1"))
                    .andExpect(jsonPath("$[1].id").value("silo2"));
            
            verify(coreDataService).getSilos();
        }
    }
    
    @Nested
    @DisplayName("getSilo() Tests")
    class GetSiloTests {
        
        @Test
        @DisplayName("GET /api/v1/silos/{id} - Found")
        void getSilo_Found() throws Exception {
            // Given
            SiloResponse silo = new SiloResponse("silo1", "Silo 1");
            when(coreDataService.getSilo("silo1")).thenReturn(silo);
            
            // When & Then
            mockMvc.perform(get("/api/v1/silos/silo1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("silo1"))
                    .andExpect(jsonPath("$.name").value("Silo 1"));
            
            verify(coreDataService).getSilo("silo1");
        }
        
        @Test
        @DisplayName("GET /api/v1/silos/{id} - Not Found")
        void getSilo_NotFound() throws Exception {
            // Given
            when(coreDataService.getSilo("invalid"))
                    .thenThrow(new ResourceNotFoundException("Silo not found: invalid"));
            
            // When & Then
            mockMvc.perform(get("/api/v1/silos/invalid"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"));
        }
    }
}
