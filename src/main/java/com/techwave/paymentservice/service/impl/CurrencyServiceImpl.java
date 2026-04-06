package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.CurrencyDto;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CurrencyMapper;
import com.techwave.paymentservice.repository.CurrencyRepository;
import com.techwave.paymentservice.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementation of {@link CurrencyService}.
 * Provides read-only operations for currency reference data.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger log =
            LoggerFactory.getLogger(CurrencyServiceImpl.class);

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public List<CurrencyDto> getAllCurrencies() {
        log.debug("Fetching all currencies");
        return currencyMapper.toDtoList(
                currencyRepository.findAll());
    }

    @Override
    public CurrencyDto getCurrencyById(String id) {
        log.debug("Fetching currency with id: {}", id);
        return currencyRepository.findById(id)
                .map(currencyMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Currency", id));
    }
}

