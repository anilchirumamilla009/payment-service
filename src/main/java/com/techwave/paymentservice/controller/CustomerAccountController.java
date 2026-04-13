package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.response.CustomerAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.service.CustomerAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CustomerAccountController {
    
    private final CustomerAccountService customerAccountService;
    
    public CustomerAccountController(CustomerAccountService customerAccountService) {
        this.customerAccountService = customerAccountService;
    }
    
    @GetMapping("/customer-accounts/{uuid}")
    public CustomerAccountResponse getCustomerAccount(@PathVariable UUID uuid) {
        return customerAccountService.getCustomerAccount(uuid);
    }
    
    @GetMapping("/customer-accounts/{uuid}/beneficial-owners")
    public List<LegalEntityResponse> getCustomerAccountBeneficialOwners(@PathVariable UUID uuid) {
        return customerAccountService.getCustomerAccountBeneficialOwners(uuid);
    }
}
