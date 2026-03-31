package com.techwave.paymentservice.api;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techwave.paymentservice.model.Corporation;
import com.techwave.paymentservice.model.CorporationAudit;
import com.techwave.paymentservice.model.Person;
import com.techwave.paymentservice.model.PersonAudit;

/**
 * Contract for legal entity endpoints.
 */
@Validated
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public interface LegalEntitiesApi {

    @PostMapping(path = "/corporations", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Corporation> createCorporation(@Valid @RequestBody Corporation corporation);

    @GetMapping("/corporations/{uuid}")
    ResponseEntity<Corporation> getCorporation(@PathVariable("uuid") UUID uuid);

    @PatchMapping(path = "/corporations/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Corporation> updateCorporation(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody Corporation corporation);

    @GetMapping("/corporations/{uuid}/audit-trail")
    ResponseEntity<List<CorporationAudit>> getCorporationAuditTrail(@PathVariable("uuid") UUID uuid);

    @GetMapping("/corporations/{country}/{code}")
    ResponseEntity<Corporation> getCorporationByCode(
            @PathVariable("country") String country,
            @PathVariable("code") String code);

    @PostMapping(path = "/people", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Person> createPerson(@Valid @RequestBody Person person);

    @GetMapping("/people/{uuid}")
    ResponseEntity<Person> getPerson(@PathVariable("uuid") UUID uuid);

    @PatchMapping(path = "/people/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Person> updatePerson(@PathVariable("uuid") UUID uuid, @Valid @RequestBody Person person);

    @GetMapping("/people/{uuid}/audit-trail")
    ResponseEntity<List<PersonAudit>> getPersonAuditTrail(@PathVariable("uuid") UUID uuid);
}

