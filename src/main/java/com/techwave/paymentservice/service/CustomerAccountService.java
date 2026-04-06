package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CustomerAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for customer account operations.
 */
public interface CustomerAccountService {

    CustomerAccountDto getCustomerAccountById(UUID uuid);

    List<LegalEntityDto> getBeneficialOwners(UUID uuid);
}

