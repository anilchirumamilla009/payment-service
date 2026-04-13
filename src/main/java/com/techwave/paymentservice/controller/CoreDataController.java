package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.dto.response.SiloResponse;
import com.techwave.paymentservice.service.CoreDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CoreDataController {
    
    private final CoreDataService coreDataService;
    
    public CoreDataController(CoreDataService coreDataService) {
        this.coreDataService = coreDataService;
    }
    
    @GetMapping("/countries")
    public List<CountryResponse> getCountries() {
        return coreDataService.getCountries();
    }
    
    @GetMapping("/countries/{id}")
    public CountryResponse getCountry(@PathVariable String id) {
        return coreDataService.getCountry(id);
    }
    
    @GetMapping("/currencies")
    public List<CurrencyResponse> getCurrencies() {
        return coreDataService.getCurrencies();
    }
    
    @GetMapping("/currencies/{id}")
    public CurrencyResponse getCurrency(@PathVariable String id) {
        return coreDataService.getCurrency(id);
    }
    
    @GetMapping("/silos")
    public List<SiloResponse> getSilos() {
        return coreDataService.getSilos();
    }
    
    @GetMapping("/silos/{id}")
    public SiloResponse getSilo(@PathVariable String id) {
        return coreDataService.getSilo(id);
    }
}
