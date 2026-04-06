package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.BankAccountAuditDto;
import com.techwave.paymentservice.dto.BankAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for bank account operations.
 */
public interface BankAccountService {

    /**
     * Creates or locates an existing bank account (idempotent PUT).
     * If an account with the same IBAN exists, it is returned.
     */
    BankAccountDto createOrLocateBankAccount(BankAccountDto dto);

    BankAccountDto getBankAccountById(UUID uuid);

    List<BankAccountAuditDto> getAuditTrail(UUID uuid);

    List<LegalEntityDto> getBeneficialOwners(UUID uuid);
}

