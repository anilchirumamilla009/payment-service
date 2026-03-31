package com.techwave.paymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.techwave.paymentservice.entity.BeneficialOwnerEntity;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.EntityMapper;
import com.techwave.paymentservice.model.CustomerAccount;
import com.techwave.paymentservice.model.LegalEntity;
import com.techwave.paymentservice.repository.BeneficialOwnerRepository;
import com.techwave.paymentservice.repository.CorporationRepository;
import com.techwave.paymentservice.repository.CustomerAccountRepository;
import com.techwave.paymentservice.repository.PersonRepository;

/**
 * Business service for customer account endpoints.
 */
@Service
public class CustomerAccountsService {

    private final CustomerAccountRepository customerAccountRepository;
    private final BeneficialOwnerRepository beneficialOwnerRepository;
    private final PersonRepository personRepository;
    private final CorporationRepository corporationRepository;
    private final EntityMapper mapper;

    public CustomerAccountsService(CustomerAccountRepository customerAccountRepository,
                                   BeneficialOwnerRepository beneficialOwnerRepository,
                                   PersonRepository personRepository,
                                   CorporationRepository corporationRepository,
                                   EntityMapper mapper) {
        this.customerAccountRepository = customerAccountRepository;
        this.beneficialOwnerRepository = beneficialOwnerRepository;
        this.personRepository = personRepository;
        this.corporationRepository = corporationRepository;
        this.mapper = mapper;
    }

    /**
     * Returns a customer account by identifier.
     */
    public CustomerAccount getCustomerAccount(UUID uuid) {
        return customerAccountRepository.findById(uuid)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer account not found for id: " + uuid));
    }

    /**
     * Returns customer account beneficial owners.
     */
    public List<LegalEntity> getCustomerAccountBeneficialOwners(UUID uuid) {
        if (!customerAccountRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("Customer account not found for id: " + uuid);
        }
        List<BeneficialOwnerEntity> owners = beneficialOwnerRepository
                .findByAccountIdAndAccountType(uuid, "CUSTOMER_ACCOUNT");
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
}
