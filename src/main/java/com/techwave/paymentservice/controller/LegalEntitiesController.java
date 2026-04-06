package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.CorporationAuditDto;
import com.techwave.paymentservice.dto.CorporationDto;
import com.techwave.paymentservice.dto.PersonAuditDto;
import com.techwave.paymentservice.dto.PersonDto;
import com.techwave.paymentservice.service.CorporationService;
import com.techwave.paymentservice.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for legal-entity endpoints:
 * people and corporations.
 * Corresponds to the "legal-entities" tag in the OpenAPI specification.
 */
@RestController
public class LegalEntitiesController {

    private static final Logger log =
            LoggerFactory.getLogger(LegalEntitiesController.class);

    private final PersonService personService;
    private final CorporationService corporationService;

    public LegalEntitiesController(PersonService personService,
                                   CorporationService corporationService) {
        this.personService = personService;
        this.corporationService = corporationService;
    }

    // ── People ───────────────────────────────────────────────

    @PostMapping(value = "/people",
                 consumes = "application/json",
                 produces = "application/json")
    public ResponseEntity<PersonDto> createPerson(
            @RequestBody PersonDto body) {
        log.debug("POST /people");
        return ResponseEntity.ok(personService.createPerson(body));
    }

    @GetMapping(value = "/people/{uuid}",
                produces = "application/json")
    public ResponseEntity<PersonDto> getPerson(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /people/{}", uuid);
        return ResponseEntity.ok(personService.getPersonById(uuid));
    }

    @PatchMapping(value = "/people/{uuid}",
                  consumes = "application/json",
                  produces = "application/json")
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable("uuid") UUID uuid,
            @RequestBody PersonDto body) {
        log.debug("PATCH /people/{}", uuid);
        return ResponseEntity.ok(
                personService.updatePerson(uuid, body));
    }

    @GetMapping(value = "/people/{uuid}/audit-trail",
                produces = "application/json")
    public ResponseEntity<List<PersonAuditDto>> getPersonAuditTrail(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /people/{}/audit-trail", uuid);
        return ResponseEntity.ok(personService.getAuditTrail(uuid));
    }

    // ── Corporations ─────────────────────────────────────────

    @PostMapping(value = "/corporations",
                 consumes = "application/json",
                 produces = "application/json")
    public ResponseEntity<CorporationDto> createCorporation(
            @RequestBody CorporationDto body) {
        log.debug("POST /corporations");
        return ResponseEntity.ok(
                corporationService.createCorporation(body));
    }

    @GetMapping(value = "/corporations/{uuid}",
                produces = "application/json")
    public ResponseEntity<CorporationDto> getCorporation(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /corporations/{}", uuid);
        return ResponseEntity.ok(
                corporationService.getCorporationById(uuid));
    }

    @PatchMapping(value = "/corporations/{uuid}",
                  consumes = "application/json",
                  produces = "application/json")
    public ResponseEntity<CorporationDto> updateCorporation(
            @PathVariable("uuid") UUID uuid,
            @RequestBody CorporationDto body) {
        log.debug("PATCH /corporations/{}", uuid);
        return ResponseEntity.ok(
                corporationService.updateCorporation(uuid, body));
    }

    @GetMapping(value = "/corporations/{uuid}/audit-trail",
                produces = "application/json")
    public ResponseEntity<List<CorporationAuditDto>>
            getCorporationAuditTrail(
                    @PathVariable("uuid") UUID uuid) {
        log.debug("GET /corporations/{}/audit-trail", uuid);
        return ResponseEntity.ok(
                corporationService.getAuditTrail(uuid));
    }

    @GetMapping(value = "/corporations/{country}/{code}",
                produces = "application/json")
    public ResponseEntity<CorporationDto> getCorporationByCode(
            @PathVariable("country") String country,
            @PathVariable("code") String code) {
        log.debug("GET /corporations/{}/{}", country, code);
        return ResponseEntity.ok(
                corporationService.getCorporationByCode(country, code));
    }
}

