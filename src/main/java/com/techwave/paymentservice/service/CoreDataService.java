package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.dto.response.SiloResponse;
import java.util.List;

public interface CoreDataService {
    List<CountryResponse> getCountries();
    CountryResponse getCountry(String id);
    List<CurrencyResponse> getCurrencies();
    CurrencyResponse getCurrency(String id);
    List<SiloResponse> getSilos();
    SiloResponse getSilo(String id);
}
