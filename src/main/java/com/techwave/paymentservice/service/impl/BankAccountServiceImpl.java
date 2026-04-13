package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.request.BankAccountRequest;
import com.techwave.paymentservice.dto.response.BankAccountAuditResponse;
import com.techwave.paymentservice.dto.response.BankAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.entity.BankAccountAudit;
import com.techwave.paymentservice.entity.BankAccountEntity;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.BankAccountMapper;
import com.techwave.paymentservice.repository.BankAccountAuditRepository;
import com.techwave.paymentservice.repository.BankAccountBeneficialOwnerRepository;
import com.techwave.paymentservice.repository.BankAccountRepository;
import com.techwave.paymentservice.service.BankAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {
    
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountAuditRepository bankAccountAuditRepository;
    private final BankAccountBeneficialOwnerRepository beneficialOwnerRepository;
    private final BankAccountMapper bankAccountMapper;
    
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                BankAccountAuditRepository bankAccountAuditRepository,
                                BankAccountBeneficialOwnerRepository beneficialOwnerRepository,
                                BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountAuditRepository = bankAccountAuditRepository;
        this.beneficialOwnerRepository = beneficialOwnerRepository;
        this.bankAccountMapper = bankAccountMapper;
    }
    
    @Override
    @Transactional
    public BankAccountResponse createOrLocateBankAccount(BankAccountRequest request) {
        // Try to locate by IBAN if provided
        if (request.getIban() != null) {
            var existing = bankAccountRepository.findByIban(request.getIban());
            if (existing.isPresent()) {
                return bankAccountMapper.toResponse(existing.get());
            }
        }
        
        // Create new bank account
        BankAccountEntity entity = bankAccountMapper.toEntity(request);
        BankAccountEntity saved = bankAccountRepository.save(entity);
        recordBankAccountAudit(saved);
        return bankAccountMapper.toResponse(saved);
    }
    
    @Override
    public BankAccountResponse getBankAccount(UUID id) {
        return bankAccountRepository.findById(id)
                .map(bankAccountMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found: " + id));
    }
    
    @Override
    public List<BankAccountAuditResponse> getBankAccountAuditTrail(UUID id) {
        // Verify the bank account exists
        if (!bankAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bank account not found: " + id);
        }
        List<BankAccountAudit> audits = bankAccountAuditRepository.findByResourceOrderByVersionDesc(id);
        return bankAccountMapper.toAuditResponses(audits);
    }
    
    @Override
    public List<LegalEntityResponse> getBankAccountBeneficialOwners(UUID id) {
        // Verify the bank account exists
        if (!bankAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bank account not found: " + id);
        }
        
        return beneficialOwnerRepository.findByIdBankAccountId(id).stream()
                .map(owner -> {
                    var legalEntity = owner.getLegalEntity();
                    String resourceType = legalEntity instanceof PersonEntity ? "people" : "corporations";
                    return new LegalEntityResponse(legalEntity.getId(), resourceType);
                })
                .collect(Collectors.toList());
    }
    
    private void recordBankAccountAudit(BankAccountEntity entity) {
        BankAccountAudit audit = new BankAccountAudit(
                entity.getId(),
                entity.getVersion(),
                entity.getBeneficiary(),
                entity.getBeneficiaryAddress(),
                entity.getNickname(),
                entity.getIban(),
                entity.getBic(),
                entity.getAccountNumber(),
                entity.getNationalBankCode(),
                entity.getNationalBranchCode(),
                entity.getNationalClearingCode(),
                entity.getCurrency(),
                entity.getCountry()
        );
        bankAccountAuditRepository.save(audit);
    }
}
