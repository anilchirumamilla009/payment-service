package com.techwave.paymentservice.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.paymentservice.api.BankAccountsApi;
import com.techwave.paymentservice.model.BankAccount;
import com.techwave.paymentservice.model.BankAccountAudit;
import com.techwave.paymentservice.model.LegalEntity;
import com.techwave.paymentservice.service.BankAccountsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for bank account endpoints.
 */
@RestController
@Tag(name = "bank-accounts", description = "Bank accounts API")
public class BankAccountsController implements BankAccountsApi {

    private final BankAccountsService bankAccountsService;

    public BankAccountsController(BankAccountsService bankAccountsService) {
        this.bankAccountsService = bankAccountsService;
    }

    @Override
    @Operation(summary = "Create or locate a bank account")
    public ResponseEntity<BankAccount> createBankAccount(@Valid BankAccount bankAccount) {
        return ResponseEntity.ok(bankAccountsService.createBankAccount(bankAccount));
    }

    @Override
    @Operation(summary = "Retrieve a single bank account by UUID")
    public ResponseEntity<BankAccount> getBankAccount(UUID uuid) {
        return ResponseEntity.ok(bankAccountsService.getBankAccount(uuid));
    }

    @Override
    @Operation(summary = "Audit trail for a single bank account")
    public ResponseEntity<List<BankAccountAudit>> getBankAccountAuditTrail(UUID uuid) {
        return ResponseEntity.ok(bankAccountsService.getBankAccountAuditTrail(uuid));
    }

    @Override
    @Operation(summary = "Beneficial owners for a single bank account")
    public ResponseEntity<List<LegalEntity>> getBankAccountBeneficialOwners(UUID uuid) {
        return ResponseEntity.ok(bankAccountsService.getBankAccountBeneficialOwners(uuid));
    }
}
