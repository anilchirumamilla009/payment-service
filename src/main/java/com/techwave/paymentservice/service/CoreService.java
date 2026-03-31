package com.techwave.paymentservice.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.EntityMapper;
import com.techwave.paymentservice.model.Country;
import com.techwave.paymentservice.model.Currency;
import com.techwave.paymentservice.model.Silo;
import com.techwave.paymentservice.repository.CountryRepository;
import com.techwave.paymentservice.repository.CurrencyRepository;
import com.techwave.paymentservice.repository.SiloRepository;

/**
 * Business service for core reference data endpoints.
 */
@Service
public class CoreService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final SiloRepository siloRepository;
    private final EntityMapper mapper;

    public CoreService(CountryRepository countryRepository,
                       CurrencyRepository currencyRepository,
                       SiloRepository siloRepository,
                       EntityMapper mapper) {
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
        this.siloRepository = siloRepository;
        this.mapper = mapper;
    }

    /**
     * Returns all configured countries.
     */
    public List<Country> getCountries() {
        return countryRepository.findAll(Sort.by("id")).stream().map(mapper::toDto).toList();
    }

    /**
     * Returns one country by identifier.
     */
    public Country getCountry(String id) {
        return countryRepository.findById(id.toUpperCase().trim())
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found for id: " + id));
    }

    /**
     * Returns all configured currencies.
     */
    public List<Currency> getCurrencies() {
        return currencyRepository.findAll(Sort.by("id")).stream().map(mapper::toDto).toList();
    }

    /**
     * Returns one currency by identifier.
     */
    public Currency getCurrency(String id) {
        return currencyRepository.findById(id.toUpperCase().trim())
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found for id: " + id));
    }

    /**
     * Returns all configured silos.
     */
    public List<Silo> getSilos() {
        return siloRepository.findAll(Sort.by("id")).stream().map(mapper::toDto).toList();
    }

    /**
     * Returns one silo by identifier.
     */
    public Silo getSilo(String id) {
        return siloRepository.findById(id.trim())
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Silo not found for id: " + id));
    }
}
