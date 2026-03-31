package com.techwave.paymentservice.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techwave.paymentservice.model.Country;
import com.techwave.paymentservice.model.Currency;
import com.techwave.paymentservice.model.Silo;

/**
 * Contract for core reference data endpoints.
 */
@Validated
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public interface CoreApi {

    @GetMapping("/countries")
    ResponseEntity<List<Country>> getCountries();

    @GetMapping("/countries/{id}")
    ResponseEntity<Country> getCountry(@PathVariable("id") String id);

    @GetMapping("/currencies")
    ResponseEntity<List<Currency>> getCurrencies();

    @GetMapping("/currencies/{id}")
    ResponseEntity<Currency> getCurrency(@PathVariable("id") String id);

    @GetMapping("/silos")
    ResponseEntity<List<Silo>> getSilos();

    @GetMapping("/silos/{id}")
    ResponseEntity<Silo> getSilo(@PathVariable("id") String id);
}

