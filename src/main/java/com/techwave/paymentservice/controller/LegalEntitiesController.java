package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.CorporationAuditDto;
import com.techwave.paymentservice.dto.CorporationDto;
import com.techwave.paymentservice.dto.PersonAuditDto;
import com.techwave.paymentservice.dto.PersonDto;
import com.techwave.paymentservice.service.CorporationService;
import com.techwave.paymentservice.service.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

/**
 * REST controller for legal-entity endpoints:
 * people and corporations.
 * Corresponds to the "legal-entities" tag in the OpenAPI specification.
 */
@RestController
@RequestMapping("/api/v1")
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
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDto> createPerson(
            @Valid @RequestBody PersonDto body) {
        log.debug("POST /people");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personService.createPerson(body));
    }

    @GetMapping(value = "/people/{uuid}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDto> getPerson(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /people/{}", uuid);
        return ResponseEntity.ok(personService.getPersonById(uuid));
    }

    @PatchMapping(value = "/people/{uuid}",
                  consumes = MediaType.APPLICATION_JSON_VALUE,
                  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody PersonDto body) {
        log.debug("PATCH /people/{}", uuid);
        return ResponseEntity.ok(
                personService.updatePerson(uuid, body));
    }

    @GetMapping(value = "/people/{uuid}/audit-trail",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonAuditDto>> getPersonAuditTrail(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /people/{}/audit-trail", uuid);
        return ResponseEntity.ok(personService.getAuditTrail(uuid));
    }

    // ── Corporations ─────────────────────────────────────────

    @PostMapping(value = "/corporations",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CorporationDto> createCorporation(
            @Valid @RequestBody CorporationDto body) {
        log.debug("POST /corporations");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(corporationService.createCorporation(body));
    }

    @GetMapping(value = "/corporations/{uuid}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CorporationDto> getCorporation(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /corporations/{}", uuid);
        return ResponseEntity.ok(
                corporationService.getCorporationById(uuid));
    }

    @PatchMapping(value = "/corporations/{uuid}",
                  consumes = MediaType.APPLICATION_JSON_VALUE,
                  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CorporationDto> updateCorporation(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody CorporationDto body) {
        log.debug("PATCH /corporations/{}", uuid);
        return ResponseEntity.ok(
                corporationService.updateCorporation(uuid, body));
    }

    @GetMapping(value = "/corporations/{uuid}/audit-trail",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CorporationAuditDto>>
            getCorporationAuditTrail(
                    @PathVariable("uuid") UUID uuid) {
        log.debug("GET /corporations/{}/audit-trail", uuid);
        return ResponseEntity.ok(
                corporationService.getAuditTrail(uuid));
    }

    @GetMapping(value = "/corporations/{country}/{code}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CorporationDto> getCorporationByCode(
            @PathVariable("country") String country,
            @PathVariable("code") String code) {
        log.debug("GET /corporations/{}/{}", country, code);
        return ResponseEntity.ok(
                corporationService.getCorporationByCode(country, code));
    }
}
