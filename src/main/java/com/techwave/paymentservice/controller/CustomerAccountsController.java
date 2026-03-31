package com.techwave.paymentservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.paymentservice.api.CustomerAccountsApi;
import com.techwave.paymentservice.model.CustomerAccount;
import com.techwave.paymentservice.model.LegalEntity;
import com.techwave.paymentservice.service.CustomerAccountsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for customer account endpoints.
 */
@RestController
@Tag(name = "customer-accounts", description = "Customer accounts API")
public class CustomerAccountsController implements CustomerAccountsApi {

    private final CustomerAccountsService customerAccountsService;

    public CustomerAccountsController(CustomerAccountsService customerAccountsService) {
        this.customerAccountsService = customerAccountsService;
    }

    @Override
    @Operation(summary = "Retrieve a single customer account by UUID")
    public ResponseEntity<CustomerAccount> getCustomerAccount(UUID uuid) {
        return ResponseEntity.ok(customerAccountsService.getCustomerAccount(uuid));
    }

    @Override
    @Operation(summary = "Beneficial owners for a single customer account")
    public ResponseEntity<List<LegalEntity>> getCustomerAccountBeneficialOwners(UUID uuid) {
        return ResponseEntity.ok(customerAccountsService.getCustomerAccountBeneficialOwners(uuid));
    }
}

