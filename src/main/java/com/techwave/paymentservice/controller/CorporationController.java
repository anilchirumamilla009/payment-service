package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.request.CorporationRequest;
import com.techwave.paymentservice.dto.response.CorporationAuditResponse;
import com.techwave.paymentservice.dto.response.CorporationResponse;
import com.techwave.paymentservice.service.LegalEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CorporationController {
    
    private final LegalEntityService legalEntityService;
    
    public CorporationController(LegalEntityService legalEntityService) {
        this.legalEntityService = legalEntityService;
    }
    
    @PostMapping("/corporations")
    public ResponseEntity<CorporationResponse> createCorporation(
            @Valid @RequestBody CorporationRequest request) {
        CorporationResponse response = legalEntityService.createCorporation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/corporations/{uuid}")
    public CorporationResponse getCorporation(@PathVariable UUID uuid) {
        return legalEntityService.getCorporation(uuid);
    }
    
    @PatchMapping("/corporations/{uuid}")
    public CorporationResponse updateCorporation(
            @PathVariable UUID uuid,
            @Valid @RequestBody CorporationRequest request) {
        return legalEntityService.updateCorporation(uuid, request);
    }
    
    @GetMapping("/corporations/{uuid}/audit-trail")
    public List<CorporationAuditResponse> getCorporationAuditTrail(@PathVariable UUID uuid) {
        return legalEntityService.getCorporationAuditTrail(uuid);
    }
    
    @GetMapping("/corporations/{country}/{code}")
    public CorporationResponse getCorporationByCode(
            @PathVariable String country,
            @PathVariable String code) {
        return legalEntityService.getCorporationByCode(country, code);
    }
}
