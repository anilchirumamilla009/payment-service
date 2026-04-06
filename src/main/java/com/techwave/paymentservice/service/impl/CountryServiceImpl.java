package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.CountryDto;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CountryMapper;
import com.techwave.paymentservice.repository.CountryRepository;
import com.techwave.paymentservice.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementation of {@link CountryService}.
 * Provides read-only operations for country reference data.
 */
@Service
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    private static final Logger log =
            LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryServiceImpl(CountryRepository countryRepository,
                              CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    @Override
    public List<CountryDto> getAllCountries() {
        log.debug("Fetching all countries");
        return countryMapper.toDtoList(
                countryRepository.findAll());
    }

    @Override
    public CountryDto getCountryById(String id) {
        log.debug("Fetching country with id: {}", id);
        return countryRepository.findById(id)
                .map(countryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Country", id));
    }
}

