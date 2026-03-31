package com.techwave.paymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techwave.paymentservice.entity.BankAccountEntity;
import com.techwave.paymentservice.entity.BeneficialOwnerEntity;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.BadRequestException;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.EntityMapper;
import com.techwave.paymentservice.model.BankAccount;
import com.techwave.paymentservice.model.BankAccountAudit;
import com.techwave.paymentservice.model.LegalEntity;
import com.techwave.paymentservice.repository.BankAccountAuditRepository;
import com.techwave.paymentservice.repository.BankAccountRepository;
import com.techwave.paymentservice.repository.BeneficialOwnerRepository;
import com.techwave.paymentservice.repository.CorporationRepository;
import com.techwave.paymentservice.repository.PersonRepository;

/**
 * Business service for bank account endpoints.
 */
@Service
public class BankAccountsService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountAuditRepository auditRepository;
    private final BeneficialOwnerRepository beneficialOwnerRepository;
    private final PersonRepository personRepository;
    private final CorporationRepository corporationRepository;
    private final EntityMapper mapper;

    public BankAccountsService(BankAccountRepository bankAccountRepository,
                               BankAccountAuditRepository auditRepository,
                               BeneficialOwnerRepository beneficialOwnerRepository,
                               PersonRepository personRepository,
                               CorporationRepository corporationRepository,
                               EntityMapper mapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.auditRepository = auditRepository;
        this.beneficialOwnerRepository = beneficialOwnerRepository;
        this.personRepository = personRepository;
        this.corporationRepository = corporationRepository;
        this.mapper = mapper;
    }

    /**
     * Creates or updates a bank account using idempotent PUT semantics.
     */
    @Transactional
    public BankAccount createBankAccount(BankAccount bankAccount) {
        validateBankAccount(bankAccount);
        if (bankAccount.getId() == null) {
            bankAccount.setId(UUID.randomUUID());
        }
        bankAccount.setResourceType("bank-accounts");

        BankAccountEntity entity = mapper.toEntity(bankAccount);
        entity = bankAccountRepository.save(entity);

        int nextVersion = auditRepository.countByResource(entity.getId()) + 1;
        auditRepository.save(mapper.toAuditEntity(entity, nextVersion));

        return mapper.toDto(entity);
    }

    /**
     * Returns a bank account by identifier.
     */
    public BankAccount getBankAccount(UUID uuid) {
        return bankAccountRepository.findById(uuid)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found for id: " + uuid));
    }

    /**
     * Returns the audit trail for a bank account.
     */
    public List<BankAccountAudit> getBankAccountAuditTrail(UUID uuid) {
        ensureBankAccountExists(uuid);
        return auditRepository.findByResourceOrderByVersionAsc(uuid).stream()
                .map(mapper::toDto).toList();
    }

    /**
     * Returns beneficial owners for a bank account.
     */
    public List<LegalEntity> getBankAccountBeneficialOwners(UUID uuid) {
        ensureBankAccountExists(uuid);
        List<BeneficialOwnerEntity> owners = beneficialOwnerRepository
                .findByAccountIdAndAccountType(uuid, "BANK_ACCOUNT");
        return resolveLegalEntities(owners);
    }

    private void ensureBankAccountExists(UUID uuid) {
        if (!bankAccountRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("Bank account not found for id: " + uuid);
        }
    }

    private List<LegalEntity> resolveLegalEntities(List<BeneficialOwnerEntity> owners) {
        List<LegalEntity> result = new ArrayList<>();
        for (BeneficialOwnerEntity bo : owners) {
            if ("PERSON".equals(bo.getOwnerType())) {
                PersonEntity person = personRepository.findById(bo.getOwnerId()).orElse(null);
                if (person != null) result.add(mapper.toLegalEntityDto(bo, person));
            } else if ("CORPORATION".equals(bo.getOwnerType())) {
                CorporationEntity corp = corporationRepository.findById(bo.getOwnerId()).orElse(null);
                if (corp != null) result.add(mapper.toLegalEntityDto(bo, corp));
            }
        }
        return result;
    }

    private void validateBankAccount(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new BadRequestException("Bank account payload is required.");
        }
        if (isBlank(bankAccount.getBeneficiary())) {
            throw new BadRequestException("Bank account beneficiary is required.");
        }
        if (isBlank(bankAccount.getCurrency())) {
            throw new BadRequestException("Bank account currency is required.");
        }
        if (isBlank(bankAccount.getCountry())) {
            throw new BadRequestException("Bank account country is required.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
