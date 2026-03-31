package com.techwave.paymentservice.api;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techwave.paymentservice.model.BankAccount;
import com.techwave.paymentservice.model.BankAccountAudit;
import com.techwave.paymentservice.model.LegalEntity;

/**
 * Contract for bank account endpoints.
 */
@Validated
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public interface BankAccountsApi {

    @PutMapping(path = "/bank-accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BankAccount> createBankAccount(@Valid @RequestBody BankAccount bankAccount);

    @GetMapping("/bank-accounts/{uuid}")
    ResponseEntity<BankAccount> getBankAccount(@PathVariable("uuid") UUID uuid);

    @GetMapping("/bank-accounts/{uuid}/audit-trail")
    ResponseEntity<List<BankAccountAudit>> getBankAccountAuditTrail(@PathVariable("uuid") UUID uuid);

    @GetMapping("/bank-accounts/{uuid}/beneficial-owners")
    ResponseEntity<List<LegalEntity>> getBankAccountBeneficialOwners(@PathVariable("uuid") UUID uuid);
}

