package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.CountryDto;
import com.techwave.paymentservice.dto.CurrencyDto;
import com.techwave.paymentservice.dto.SiloDto;
import com.techwave.paymentservice.service.CountryService;
import com.techwave.paymentservice.service.CurrencyService;
import com.techwave.paymentservice.service.SiloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for core reference-data endpoints:
 * countries, currencies, and silos.
 * Corresponds to the "core" tag in the OpenAPI specification.
 */
@RestController
@RequestMapping("/api/v1")
public class CoreController {

    private static final Logger log =
            LoggerFactory.getLogger(CoreController.class);

    private final CountryService countryService;
    private final CurrencyService currencyService;
    private final SiloService siloService;

    public CoreController(CountryService countryService,
                          CurrencyService currencyService,
                          SiloService siloService) {
        this.countryService = countryService;
        this.currencyService = currencyService;
        this.siloService = siloService;
    }

    // ── Countries ────────────────────────────────────────────

    @GetMapping(value = "/countries",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CountryDto>> getCountries() {
        log.debug("GET /countries");
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping(value = "/countries/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryDto> getCountry(
            @PathVariable("id") String id) {
        log.debug("GET /countries/{}", id);
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    // ── Currencies ───────────────────────────────────────────

    @GetMapping(value = "/currencies",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CurrencyDto>> getCurrencies() {
        log.debug("GET /currencies");
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping(value = "/currencies/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyDto> getCurrency(
            @PathVariable("id") String id) {
        log.debug("GET /currencies/{}", id);
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    // ── Silos ────────────────────────────────────────────────

    @GetMapping(value = "/silos",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SiloDto>> getSilos() {
        log.debug("GET /silos");
        return ResponseEntity.ok(siloService.getAllSilos());
    }

    @GetMapping(value = "/silos/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SiloDto> getSilo(
            @PathVariable("id") String id) {
        log.debug("GET /silos/{}", id);
        return ResponseEntity.ok(siloService.getSiloById(id));
    }
}
