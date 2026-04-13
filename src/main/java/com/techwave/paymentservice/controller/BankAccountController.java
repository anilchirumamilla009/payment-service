package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.request.BankAccountRequest;
import com.techwave.paymentservice.dto.response.BankAccountAuditResponse;
import com.techwave.paymentservice.dto.response.BankAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class BankAccountController {
    
    private final BankAccountService bankAccountService;
    
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    
    @PutMapping("/bank-accounts")
    public ResponseEntity<BankAccountResponse> createBankAccount(
            @Valid @RequestBody BankAccountRequest request) {
        BankAccountResponse response = bankAccountService.createOrLocateBankAccount(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/bank-accounts/{uuid}")
    public BankAccountResponse getBankAccount(@PathVariable UUID uuid) {
        return bankAccountService.getBankAccount(uuid);
    }
    
    @GetMapping("/bank-accounts/{uuid}/audit-trail")
    public List<BankAccountAuditResponse> getBankAccountAuditTrail(@PathVariable UUID uuid) {
        return bankAccountService.getBankAccountAuditTrail(uuid);
    }
    
    @GetMapping("/bank-accounts/{uuid}/beneficial-owners")
    public List<LegalEntityResponse> getBankAccountBeneficialOwners(@PathVariable UUID uuid) {
        return bankAccountService.getBankAccountBeneficialOwners(uuid);
    }
}
