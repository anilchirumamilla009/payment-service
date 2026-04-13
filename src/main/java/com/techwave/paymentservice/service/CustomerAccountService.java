package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.response.CustomerAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import java.util.List;
import java.util.UUID;

public interface CustomerAccountService {
    CustomerAccountResponse getCustomerAccount(UUID id);
    List<LegalEntityResponse> getCustomerAccountBeneficialOwners(UUID id);
}
