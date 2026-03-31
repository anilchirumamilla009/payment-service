package com.techwave.paymentservice.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techwave.paymentservice.model.CustomerAccount;
import com.techwave.paymentservice.model.LegalEntity;

/**
 * Contract for customer account endpoints.
 */
@Validated
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public interface CustomerAccountsApi {

    @GetMapping("/customer-accounts/{uuid}")
    ResponseEntity<CustomerAccount> getCustomerAccount(@PathVariable("uuid") UUID uuid);

    @GetMapping("/customer-accounts/{uuid}/beneficial-owners")
    ResponseEntity<List<LegalEntity>> getCustomerAccountBeneficialOwners(@PathVariable("uuid") UUID uuid);
}

