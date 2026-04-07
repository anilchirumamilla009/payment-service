package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.BankAccountAuditDto;
import com.techwave.paymentservice.dto.BankAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.service.BankAccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for bank-account endpoints.
 * Corresponds to the "bank-accounts" tag in the OpenAPI specification.
 */
@RestController
@RequestMapping("/api/v1")
public class BankAccountsController {

    private static final Logger log =
            LoggerFactory.getLogger(BankAccountsController.class);

    private final BankAccountService bankAccountService;

    public BankAccountsController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PutMapping(value = "/bank-accounts",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccountDto> createBankAccount(
            @Valid @RequestBody BankAccountDto body) {
        log.debug("PUT /bank-accounts");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankAccountService.createOrLocateBankAccount(body));
    }

    @GetMapping(value = "/bank-accounts/{uuid}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccountDto> getBankAccount(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /bank-accounts/{}", uuid);
        return ResponseEntity.ok(
                bankAccountService.getBankAccountById(uuid));
    }

    @GetMapping(value = "/bank-accounts/{uuid}/audit-trail",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BankAccountAuditDto>>
            getBankAccountAuditTrail(
                    @PathVariable("uuid") UUID uuid) {
        log.debug("GET /bank-accounts/{}/audit-trail", uuid);
        return ResponseEntity.ok(
                bankAccountService.getAuditTrail(uuid));
    }

    @GetMapping(value = "/bank-accounts/{uuid}/beneficial-owners",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LegalEntityDto>>
            getBankAccountBeneficialOwners(
                    @PathVariable("uuid") UUID uuid) {
        log.debug("GET /bank-accounts/{}/beneficial-owners", uuid);
        return ResponseEntity.ok(
                bankAccountService.getBeneficialOwners(uuid));
    }
}
