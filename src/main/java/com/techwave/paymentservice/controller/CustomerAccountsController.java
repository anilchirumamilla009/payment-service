package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.CustomerAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.service.CustomerAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for customer-account endpoints.
 * Corresponds to the "customer-accounts" tag in the OpenAPI specification.
 */
@RestController
@RequestMapping("/api/v1")
public class CustomerAccountsController {

    private static final Logger log =
            LoggerFactory.getLogger(CustomerAccountsController.class);

    private final CustomerAccountService customerAccountService;

    public CustomerAccountsController(
            CustomerAccountService customerAccountService) {
        this.customerAccountService = customerAccountService;
    }

    @GetMapping(value = "/customer-accounts/{uuid}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerAccountDto> getCustomerAccount(
            @PathVariable("uuid") UUID uuid) {
        log.debug("GET /customer-accounts/{}", uuid);
        return ResponseEntity.ok(
                customerAccountService.getCustomerAccountById(uuid));
    }

    @GetMapping(value = "/customer-accounts/{uuid}/beneficial-owners",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LegalEntityDto>>
            getCustomerAccountBeneficialOwners(
                    @PathVariable("uuid") UUID uuid) {
        log.debug("GET /customer-accounts/{}/beneficial-owners", uuid);
        return ResponseEntity.ok(
                customerAccountService.getBeneficialOwners(uuid));
    }
}
