package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.request.BankAccountRequest;
import com.techwave.paymentservice.dto.response.BankAccountAuditResponse;
import com.techwave.paymentservice.dto.response.BankAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import java.util.List;
import java.util.UUID;

public interface BankAccountService {
    BankAccountResponse createOrLocateBankAccount(BankAccountRequest request);
    BankAccountResponse getBankAccount(UUID id);
    List<BankAccountAuditResponse> getBankAccountAuditTrail(UUID id);
    List<LegalEntityResponse> getBankAccountBeneficialOwners(UUID id);
}
