package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.dto.response.SiloResponse;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CountryMapper;
import com.techwave.paymentservice.mapper.CurrencyMapper;
import com.techwave.paymentservice.mapper.SiloMapper;
import com.techwave.paymentservice.repository.CountryRepository;
import com.techwave.paymentservice.repository.CurrencyRepository;
import com.techwave.paymentservice.repository.SiloRepository;
import com.techwave.paymentservice.service.CoreDataService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoreDataServiceImpl implements CoreDataService {
    
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final SiloRepository siloRepository;
    private final CountryMapper countryMapper;
    private final CurrencyMapper currencyMapper;
    private final SiloMapper siloMapper;
    
    public CoreDataServiceImpl(CountryRepository countryRepository,
                            CurrencyRepository currencyRepository,
                            SiloRepository siloRepository,
                            CountryMapper countryMapper,
                            CurrencyMapper currencyMapper,
                            SiloMapper siloMapper) {
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
        this.siloRepository = siloRepository;
        this.countryMapper = countryMapper;
        this.currencyMapper = currencyMapper;
        this.siloMapper = siloMapper;
    }
    
    @Override
    public List<CountryResponse> getCountries() {
        return countryMapper.toResponses(countryRepository.findAll());
    }
    
    @Override
    public CountryResponse getCountry(String id) {
        return countryRepository.findById(id)
                .map(countryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found: " + id));
    }
    
    @Override
    public List<CurrencyResponse> getCurrencies() {
        return currencyMapper.toResponses(currencyRepository.findAll());
    }
    
    @Override
    public CurrencyResponse getCurrency(String id) {
        return currencyRepository.findById(id)
                .map(currencyMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found: " + id));
    }
    
    @Override
    public List<SiloResponse> getSilos() {
        return siloMapper.toResponses(siloRepository.findAll());
    }
    
    @Override
    public SiloResponse getSilo(String id) {
        return siloRepository.findById(id)
                .map(siloMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Silo not found: " + id));
    }
}
