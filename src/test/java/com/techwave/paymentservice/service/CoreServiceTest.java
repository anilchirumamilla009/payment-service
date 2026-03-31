package com.techwave.paymentservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.model.Country;
import com.techwave.paymentservice.model.Currency;
import com.techwave.paymentservice.model.Silo;

@SpringBootTest
class CoreServiceTest {

    @Autowired
    private CoreService coreService;

    @Test
    void getCountries_returnsNonEmptyList() {
        List<Country> countries = coreService.getCountries();
        assertFalse(countries.isEmpty());
        assertEquals("countries", countries.get(0).getResourceType());
    }

    @Test
    void getCountry_existingId_returnsCountry() {
        Country country = coreService.getCountry("GB");
        assertEquals("GB", country.getId());
        assertEquals("United Kingdom", country.getName());
    }

    @Test
    void getCountry_caseInsensitive_returnsCountry() {
        Country country = coreService.getCountry("gb");
        assertEquals("GB", country.getId());
    }

    @Test
    void getCountry_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> coreService.getCountry("ZZ"));
    }

    @Test
    void getCurrencies_returnsNonEmptyList() {
        List<Currency> currencies = coreService.getCurrencies();
        assertFalse(currencies.isEmpty());
    }

    @Test
    void getCurrency_existingId_returnsCurrency() {
        Currency currency = coreService.getCurrency("EUR");
        assertEquals("EUR", currency.getId());
        assertEquals("Euro", currency.getName());
    }

    @Test
    void getCurrency_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> coreService.getCurrency("XYZ"));
    }

    @Test
    void getSilos_returnsNonEmptyList() {
        List<Silo> silos = coreService.getSilos();
        assertFalse(silos.isEmpty());
    }

    @Test
    void getSilo_existingId_returnsSilo() {
        Silo silo = coreService.getSilo("treasury-core");
        assertEquals("treasury-core", silo.getId());
        assertEquals("silos", silo.getResourceType());
    }

    @Test
    void getSilo_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> coreService.getSilo("nonexistent"));
    }
}

