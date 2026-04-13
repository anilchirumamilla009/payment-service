package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.response.CustomerAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CustomerAccountMapper;
import com.techwave.paymentservice.repository.CustomerAccountBeneficialOwnerRepository;
import com.techwave.paymentservice.repository.CustomerAccountRepository;
import com.techwave.paymentservice.service.CustomerAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CustomerAccountServiceImpl implements CustomerAccountService {
    
    private final CustomerAccountRepository customerAccountRepository;
    private final CustomerAccountBeneficialOwnerRepository beneficialOwnerRepository;
    private final CustomerAccountMapper customerAccountMapper;
    
    public CustomerAccountServiceImpl(CustomerAccountRepository customerAccountRepository,
                                    CustomerAccountBeneficialOwnerRepository beneficialOwnerRepository,
                                    CustomerAccountMapper customerAccountMapper) {
        this.customerAccountRepository = customerAccountRepository;
        this.beneficialOwnerRepository = beneficialOwnerRepository;
        this.customerAccountMapper = customerAccountMapper;
    }
    
    @Override
    public CustomerAccountResponse getCustomerAccount(UUID id) {
        return customerAccountRepository.findById(id)
                .map(customerAccountMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer account not found: " + id));
    }
    
    @Override
    public List<LegalEntityResponse> getCustomerAccountBeneficialOwners(UUID id) {
        // Verify the customer account exists
        if (!customerAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer account not found: " + id);
        }
        
        return beneficialOwnerRepository.findByIdCustomerAccountId(id).stream()
                .map(owner -> {
                    var legalEntity = owner.getLegalEntity();
                    String resourceType = legalEntity instanceof PersonEntity ? "people" : "corporations";
                    return new LegalEntityResponse(legalEntity.getId(), resourceType);
                })
                .collect(Collectors.toList());
    }
}
