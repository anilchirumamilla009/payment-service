package com.techwave.paymentservice.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.paymentservice.api.LegalEntitiesApi;
import com.techwave.paymentservice.model.Corporation;
import com.techwave.paymentservice.model.CorporationAudit;
import com.techwave.paymentservice.model.Person;
import com.techwave.paymentservice.model.PersonAudit;
import com.techwave.paymentservice.service.LegalEntitiesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for legal entity endpoints (corporations &amp; people).
 */
@RestController
@Tag(name = "legal-entities", description = "Legal entities API")
public class LegalEntitiesController implements LegalEntitiesApi {

    private final LegalEntitiesService legalEntitiesService;

    public LegalEntitiesController(LegalEntitiesService legalEntitiesService) {
        this.legalEntitiesService = legalEntitiesService;
    }

    @Override
    @Operation(summary = "Create a single corporation")
    public ResponseEntity<Corporation> createCorporation(@Valid Corporation corporation) {
        return ResponseEntity.ok(legalEntitiesService.createCorporation(corporation));
    }

    @Override
    @Operation(summary = "Retrieve a single corporation by UUID")
    public ResponseEntity<Corporation> getCorporation(UUID uuid) {
        return ResponseEntity.ok(legalEntitiesService.getCorporation(uuid));
    }

    @Override
    @Operation(summary = "Update a single corporation")
    public ResponseEntity<Corporation> updateCorporation(UUID uuid, @Valid Corporation corporation) {
        return ResponseEntity.ok(legalEntitiesService.updateCorporation(uuid, corporation));
    }

    @Override
    @Operation(summary = "Audit trail for a single corporation")
    public ResponseEntity<List<CorporationAudit>> getCorporationAuditTrail(UUID uuid) {
        return ResponseEntity.ok(legalEntitiesService.getCorporationAuditTrail(uuid));
    }

    @Override
    @Operation(summary = "Retrieve a single corporation by country and code")
    public ResponseEntity<Corporation> getCorporationByCode(String country, String code) {
        return ResponseEntity.ok(legalEntitiesService.getCorporationByCode(country, code));
    }

    @Override
    @Operation(summary = "Create a single person")
    public ResponseEntity<Person> createPerson(@Valid Person person) {
        return ResponseEntity.ok(legalEntitiesService.createPerson(person));
    }

    @Override
    @Operation(summary = "Retrieve a single person by UUID")
    public ResponseEntity<Person> getPerson(UUID uuid) {
        return ResponseEntity.ok(legalEntitiesService.getPerson(uuid));
    }

    @Override
    @Operation(summary = "Update a single person")
    public ResponseEntity<Person> updatePerson(UUID uuid, @Valid Person person) {
        return ResponseEntity.ok(legalEntitiesService.updatePerson(uuid, person));
    }

    @Override
    @Operation(summary = "Audit trail for a single person")
    public ResponseEntity<List<PersonAudit>> getPersonAuditTrail(UUID uuid) {
        return ResponseEntity.ok(legalEntitiesService.getPersonAuditTrail(uuid));
    }
}
