package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.request.PersonRequest;
import com.techwave.paymentservice.dto.response.PersonAuditResponse;
import com.techwave.paymentservice.dto.response.PersonResponse;
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
public class PersonController {
    
    private final LegalEntityService legalEntityService;
    
    public PersonController(LegalEntityService legalEntityService) {
        this.legalEntityService = legalEntityService;
    }
    
    @PostMapping("/people")
    public ResponseEntity<PersonResponse> createPerson(
            @Valid @RequestBody PersonRequest request) {
        PersonResponse response = legalEntityService.createPerson(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/people/{uuid}")
    public PersonResponse getPerson(@PathVariable UUID uuid) {
        return legalEntityService.getPerson(uuid);
    }
    
    @PatchMapping("/people/{uuid}")
    public PersonResponse updatePerson(
            @PathVariable UUID uuid,
            @Valid @RequestBody PersonRequest request) {
        return legalEntityService.updatePerson(uuid, request);
    }
    
    @GetMapping("/people/{uuid}/audit-trail")
    public List<PersonAuditResponse> getPersonAuditTrail(@PathVariable UUID uuid) {
        return legalEntityService.getPersonAuditTrail(uuid);
    }
}
