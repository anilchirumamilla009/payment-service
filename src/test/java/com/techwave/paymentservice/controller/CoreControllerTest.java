package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.CountryDto;
import com.techwave.paymentservice.dto.CurrencyDto;
import com.techwave.paymentservice.dto.SiloDto;
import com.techwave.paymentservice.service.CountryService;
import com.techwave.paymentservice.service.CurrencyService;
import com.techwave.paymentservice.service.SiloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CoreController Unit Tests")
class CoreControllerTest {

    @Mock
    private CountryService countryService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private SiloService siloService;

    @InjectMocks
    private CoreController controller;

    // ── Countries ────────────────────────────────────────────

    @Test
    @DisplayName("getCountries returns 200 with list")
    void getCountries_returns200() {
        CountryDto dto = new CountryDto();
        dto.setId("US");
        dto.setName("United States");

        when(countryService.getAllCountries()).thenReturn(List.of(dto));

        ResponseEntity<List<CountryDto>> response =
                controller.getCountries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("US", response.getBody().get(0).getId());
    }

    @Test
    @DisplayName("getCountry returns 200 with single DTO")
    void getCountry_returns200() {
        CountryDto dto = new CountryDto();
        dto.setId("US");
        dto.setName("United States");

        when(countryService.getCountryById("US")).thenReturn(dto);

        ResponseEntity<CountryDto> response =
                controller.getCountry("US");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("US", response.getBody().getId());
    }

    // ── Currencies ───────────────────────────────────────────

    @Test
    @DisplayName("getCurrencies returns 200 with list")
    void getCurrencies_returns200() {
        CurrencyDto dto = new CurrencyDto();
        dto.setId("EUR");
        dto.setName("Euro");

        when(currencyService.getAllCurrencies()).thenReturn(List.of(dto));

        ResponseEntity<List<CurrencyDto>> response =
                controller.getCurrencies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("getCurrency returns 200 with single DTO")
    void getCurrency_returns200() {
        CurrencyDto dto = new CurrencyDto();
        dto.setId("EUR");
        dto.setName("Euro");

        when(currencyService.getCurrencyById("EUR")).thenReturn(dto);

        ResponseEntity<CurrencyDto> response =
                controller.getCurrency("EUR");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EUR", response.getBody().getId());
    }

    // ── Silos ────────────────────────────────────────────────

    @Test
    @DisplayName("getSilos returns 200 with list")
    void getSilos_returns200() {
        SiloDto dto = new SiloDto();
        dto.setId("TREASURY");
        dto.setName("Treasury");

        when(siloService.getAllSilos()).thenReturn(List.of(dto));

        ResponseEntity<List<SiloDto>> response =
                controller.getSilos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("getSilo returns 200 with single DTO")
    void getSilo_returns200() {
        SiloDto dto = new SiloDto();
        dto.setId("TREASURY");
        dto.setName("Treasury");

        when(siloService.getSiloById("TREASURY")).thenReturn(dto);

        ResponseEntity<SiloDto> response =
                controller.getSilo("TREASURY");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("TREASURY", response.getBody().getId());
    }
}

