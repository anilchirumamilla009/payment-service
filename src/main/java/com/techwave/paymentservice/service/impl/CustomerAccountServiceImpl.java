package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.CustomerAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CustomerAccountMapper;
import com.techwave.paymentservice.repository.CustomerAccountRepository;
import com.techwave.paymentservice.service.CustomerAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link CustomerAccountService}.
 * Provides read-only access to customer accounts and their
 * beneficial owners.
 */
@Service
@Transactional(readOnly = true)
public class CustomerAccountServiceImpl implements CustomerAccountService {

    private static final Logger log =
            LoggerFactory.getLogger(CustomerAccountServiceImpl.class);

    private final CustomerAccountRepository customerAccountRepository;
    private final CustomerAccountMapper customerAccountMapper;

    public CustomerAccountServiceImpl(
            CustomerAccountRepository customerAccountRepository,
            CustomerAccountMapper customerAccountMapper) {
        this.customerAccountRepository = customerAccountRepository;
        this.customerAccountMapper = customerAccountMapper;
    }

    @Override
    public CustomerAccountDto getCustomerAccountById(UUID uuid) {
        log.debug("Fetching customer account with id: {}", uuid);
        return customerAccountRepository.findById(uuid)
                .map(customerAccountMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CustomerAccount", uuid.toString()));
    }

    @Override
    public List<LegalEntityDto> getBeneficialOwners(UUID uuid) {
        log.debug("Fetching beneficial owners for customer account: {}",
                uuid);
        CustomerAccountEntity entity =
                customerAccountRepository.findById(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "CustomerAccount", uuid.toString()));
        return customerAccountMapper
                .toLegalEntityDtoList(entity.getBeneficialOwners());
    }
}

