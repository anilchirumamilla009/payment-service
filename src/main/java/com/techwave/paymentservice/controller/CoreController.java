package com.techwave.paymentservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.paymentservice.api.CoreApi;
import com.techwave.paymentservice.model.Country;
import com.techwave.paymentservice.model.Currency;
import com.techwave.paymentservice.model.Silo;
import com.techwave.paymentservice.service.CoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for core reference data (countries, currencies, silos).
 */
@RestController
@Tag(name = "core", description = "Core reference data API")
public class CoreController implements CoreApi {

    private final CoreService coreService;

    public CoreController(CoreService coreService) {
        this.coreService = coreService;
    }

    @Override
    @Operation(summary = "Retrieve entire country collection")
    public ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity.ok(coreService.getCountries());
    }

    @Override
    @Operation(summary = "Retrieve single country")
    public ResponseEntity<Country> getCountry(String id) {
        return ResponseEntity.ok(coreService.getCountry(id));
    }

    @Override
    @Operation(summary = "Retrieve entire currency collection")
    public ResponseEntity<List<Currency>> getCurrencies() {
        return ResponseEntity.ok(coreService.getCurrencies());
    }

    @Override
    @Operation(summary = "Retrieve single currency")
    public ResponseEntity<Currency> getCurrency(String id) {
        return ResponseEntity.ok(coreService.getCurrency(id));
    }

    @Override
    @Operation(summary = "Retrieve entire silo collection")
    public ResponseEntity<List<Silo>> getSilos() {
        return ResponseEntity.ok(coreService.getSilos());
    }

    @Override
    @Operation(summary = "Retrieve single silo")
    public ResponseEntity<Silo> getSilo(String id) {
        return ResponseEntity.ok(coreService.getSilo(id));
    }
}

