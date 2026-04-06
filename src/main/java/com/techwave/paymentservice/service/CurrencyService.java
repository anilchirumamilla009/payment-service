package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CurrencyDto;
import java.util.List;

/**
 * Service interface for currency operations.
 */
public interface CurrencyService {

    List<CurrencyDto> getAllCurrencies();

    CurrencyDto getCurrencyById(String id);
}

