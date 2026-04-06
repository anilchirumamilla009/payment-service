package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CountryDto;
import java.util.List;

/**
 * Service interface for country operations.
 */
public interface CountryService {

    /**
     * Retrieves all countries.
     *
     * @return list of all country DTOs
     */
    List<CountryDto> getAllCountries();

    /**
     * Retrieves a single country by its Alpha-2 code.
     *
     * @param id the ISO 3166-1 Alpha-2 country code
     * @return the country DTO
     */
    CountryDto getCountryById(String id);
}

