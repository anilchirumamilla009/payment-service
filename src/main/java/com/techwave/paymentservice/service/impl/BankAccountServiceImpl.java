package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.BankAccountAuditDto;
import com.techwave.paymentservice.dto.BankAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.entity.BankAccountAuditEntity;
import com.techwave.paymentservice.entity.BankAccountEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.BankAccountMapper;
import com.techwave.paymentservice.repository.BankAccountAuditRepository;
import com.techwave.paymentservice.repository.BankAccountRepository;
import com.techwave.paymentservice.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link BankAccountService}.
 * The createOrLocate method is idempotent: if an account with the
 * same IBAN already exists it is returned instead of creating a duplicate.
 */
@Service
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger log =
            LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private static final String RESOURCE_TYPE = "BankAccount";

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountAuditRepository bankAccountAuditRepository;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountServiceImpl(
            BankAccountRepository bankAccountRepository,
            BankAccountAuditRepository bankAccountAuditRepository,
            BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountAuditRepository = bankAccountAuditRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    @Transactional
    public BankAccountDto createOrLocateBankAccount(BankAccountDto dto) {
        String maskedIban = maskIban(dto.getIban());
        log.debug("Create-or-locate bank account, iban: {}", maskedIban);

        // Idempotent: return existing account if IBAN matches
        if (dto.getIban() != null) {
            Optional<BankAccountEntity> existing =
                    bankAccountRepository.findByIban(dto.getIban());
            if (existing.isPresent()) {
                log.info("Bank account already exists for iban: {}",
                        maskedIban);
                return bankAccountMapper.toDto(existing.get());
            }
        }

        BankAccountEntity entity = bankAccountMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setVersion(1);

        BankAccountEntity saved = bankAccountRepository.save(entity);
        createAuditRecord(saved);

        log.info("Bank account created with id: {}", saved.getId());
        return bankAccountMapper.toDto(saved);
    }

    @Override
    public BankAccountDto getBankAccountById(UUID uuid) {
        log.debug("Fetching bank account with id: {}", uuid);
        return bankAccountRepository.findById(uuid)
                .map(bankAccountMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_TYPE, uuid.toString()));
    }

    @Override
    public List<BankAccountAuditDto> getAuditTrail(UUID uuid) {
        log.debug("Fetching audit trail for bank account: {}", uuid);
        if (!bankAccountRepository.existsById(uuid)) {
            throw new ResourceNotFoundException(
                    RESOURCE_TYPE, uuid.toString());
        }
        List<BankAccountAuditEntity> audits =
                bankAccountAuditRepository
                        .findByResourceOrderByVersionAsc(uuid);
        return bankAccountMapper.toAuditDtoList(audits);
    }

    @Override
    public List<LegalEntityDto> getBeneficialOwners(UUID uuid) {
        log.debug("Fetching beneficial owners for bank account: {}",
                uuid);
        BankAccountEntity entity = bankAccountRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_TYPE, uuid.toString()));
        return bankAccountMapper
                .toLegalEntityDtoList(entity.getBeneficialOwners());
    }

    private void createAuditRecord(BankAccountEntity entity) {
        BankAccountAuditEntity audit = new BankAccountAuditEntity();
        audit.setResource(entity.getId());
        audit.setVersion(entity.getVersion());
        audit.setBeneficiary(entity.getBeneficiary());
        audit.setBeneficiaryAddress(entity.getBeneficiaryAddress());
        audit.setNickname(entity.getNickname());
        audit.setIban(entity.getIban());
        audit.setBic(entity.getBic());
        audit.setAccountNumber(entity.getAccountNumber());
        audit.setNationalBankCode(entity.getNationalBankCode());
        audit.setNationalBranchCode(entity.getNationalBranchCode());
        audit.setNationalClearingCode(entity.getNationalClearingCode());
        audit.setCurrency(entity.getCurrency());
        audit.setCountry(entity.getCountry());
        bankAccountAuditRepository.save(audit);
    }

    /**
     * Masks an IBAN for safe logging, showing only the first 4 characters.
     */
    private String maskIban(String iban) {
        if (iban == null || iban.length() <= 4) {
            return "****";
        }
        return iban.substring(0, 4) + "***";
    }
}
