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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link BankAccountService}.
 * The createOrLocate method is idempotent: if an account with the
 * same IBAN already exists it is returned instead of creating a duplicate.
 */
@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger log =
            LoggerFactory.getLogger(BankAccountServiceImpl.class);

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
    public BankAccountDto createOrLocateBankAccount(BankAccountDto dto) {
        log.debug("Create-or-locate bank account, iban: {}", dto.getIban());

        // Idempotent: return existing account if IBAN matches
        if (dto.getIban() != null) {
            Optional<BankAccountEntity> existing =
                    bankAccountRepository.findByIban(dto.getIban());
            if (existing.isPresent()) {
                log.info("Bank account already exists for iban: {}",
                        dto.getIban());
                return bankAccountMapper.toDto(existing.get());
            }
        }

        BankAccountEntity entity = bankAccountMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setVersion(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        BankAccountEntity saved = bankAccountRepository.save(entity);
        createAuditRecord(saved);

        log.info("Bank account created with id: {}", saved.getId());
        return bankAccountMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountDto getBankAccountById(UUID uuid) {
        log.debug("Fetching bank account with id: {}", uuid);
        return bankAccountRepository.findById(uuid)
                .map(bankAccountMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BankAccount", uuid.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccountAuditDto> getAuditTrail(UUID uuid) {
        log.debug("Fetching audit trail for bank account: {}", uuid);
        if (!bankAccountRepository.existsById(uuid)) {
            throw new ResourceNotFoundException(
                    "BankAccount", uuid.toString());
        }
        List<BankAccountAuditEntity> audits =
                bankAccountAuditRepository
                        .findByResourceOrderByVersionAsc(uuid);
        return bankAccountMapper.toAuditDtoList(audits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalEntityDto> getBeneficialOwners(UUID uuid) {
        log.debug("Fetching beneficial owners for bank account: {}",
                uuid);
        BankAccountEntity entity = bankAccountRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BankAccount", uuid.toString()));
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
        audit.setCreatedAt(LocalDateTime.now());
        bankAccountAuditRepository.save(audit);
    }
}

