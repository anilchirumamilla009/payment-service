package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.request.CorporationRequest;
import com.techwave.paymentservice.dto.request.PersonRequest;
import com.techwave.paymentservice.dto.response.CorporationAuditResponse;
import com.techwave.paymentservice.dto.response.CorporationResponse;
import com.techwave.paymentservice.dto.response.PersonAuditResponse;
import com.techwave.paymentservice.dto.response.PersonResponse;
import com.techwave.paymentservice.entity.CorporationAudit;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.PersonAudit;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CorporationMapper;
import com.techwave.paymentservice.mapper.PersonMapper;
import com.techwave.paymentservice.repository.CorporationAuditRepository;
import com.techwave.paymentservice.repository.CorporationRepository;
import com.techwave.paymentservice.repository.PersonAuditRepository;
import com.techwave.paymentservice.repository.PersonRepository;
import com.techwave.paymentservice.service.LegalEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LegalEntityServiceImpl implements LegalEntityService {
    
    private final CorporationRepository corporationRepository;
    private final PersonRepository personRepository;
    private final CorporationAuditRepository corporationAuditRepository;
    private final PersonAuditRepository personAuditRepository;
    private final CorporationMapper corporationMapper;
    private final PersonMapper personMapper;
    
    public LegalEntityServiceImpl(CorporationRepository corporationRepository,
                                PersonRepository personRepository,
                                CorporationAuditRepository corporationAuditRepository,
                                PersonAuditRepository personAuditRepository,
                                CorporationMapper corporationMapper,
                                PersonMapper personMapper) {
        this.corporationRepository = corporationRepository;
        this.personRepository = personRepository;
        this.corporationAuditRepository = corporationAuditRepository;
        this.personAuditRepository = personAuditRepository;
        this.corporationMapper = corporationMapper;
        this.personMapper = personMapper;
    }
    
    @Override
    @Transactional
    public CorporationResponse createCorporation(CorporationRequest request) {
        CorporationEntity entity = corporationMapper.toEntity(request);
        CorporationEntity saved = corporationRepository.save(entity);
        recordCorporationAudit(saved);
        return corporationMapper.toResponse(saved);
    }
    
    @Override
    public CorporationResponse getCorporation(UUID id) {
        return corporationRepository.findById(id)
                .map(corporationMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Corporation not found: " + id));
    }
    
    @Override
    @Transactional
    public CorporationResponse updateCorporation(UUID id, CorporationRequest request) {
        CorporationEntity entity = corporationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Corporation not found: " + id));
        
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setIncorporationDate(request.getIncorporationDate());
        entity.setIncorporationCountry(request.getIncorporationCountry());
        entity.setType(request.getType());
        
        CorporationEntity updated = corporationRepository.save(entity);
        recordCorporationAudit(updated);
        return corporationMapper.toResponse(updated);
    }
    
    @Override
    public CorporationResponse getCorporationByCode(String country, String code) {
        return corporationRepository.findByCodeAndIncorporationCountry(code, country)
                .map(corporationMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Corporation not found - country: " + country + ", code: " + code));
    }
    
    @Override
    public List<CorporationAuditResponse> getCorporationAuditTrail(UUID id) {
        // Verify the corporation exists
        if (!corporationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Corporation not found: " + id);
        }
        List<CorporationAudit> audits = corporationAuditRepository.findByResourceOrderByVersionDesc(id);
        return corporationMapper.toAuditResponses(audits);
    }
    
    @Override
    @Transactional
    public PersonResponse createPerson(PersonRequest request) {
        PersonEntity entity = personMapper.toEntity(request);
        PersonEntity saved = personRepository.save(entity);
        recordPersonAudit(saved);
        return personMapper.toResponse(saved);
    }
    
    @Override
    public PersonResponse getPerson(UUID id) {
        return personRepository.findById(id)
                .map(personMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found: " + id));
    }
    
    @Override
    @Transactional
    public PersonResponse updatePerson(UUID id, PersonRequest request) {
        PersonEntity entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found: " + id));
        
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        
        PersonEntity updated = personRepository.save(entity);
        recordPersonAudit(updated);
        return personMapper.toResponse(updated);
    }
    
    @Override
    public List<PersonAuditResponse> getPersonAuditTrail(UUID id) {
        // Verify the person exists
        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person not found: " + id);
        }
        List<PersonAudit> audits = personAuditRepository.findByResourceOrderByVersionDesc(id);
        return personMapper.toAuditResponses(audits);
    }
    
    private void recordCorporationAudit(CorporationEntity entity) {
        CorporationAudit audit = new CorporationAudit(
                entity.getId(),
                entity.getVersion(),
                entity.getName(),
                entity.getCode(),
                entity.getIncorporationDate(),
                entity.getIncorporationCountry(),
                entity.getType(),
                entity.getDuplicates()
        );
        corporationAuditRepository.save(audit);
    }
    
    private void recordPersonAudit(PersonEntity entity) {
        PersonAudit audit = new PersonAudit(
                entity.getId(),
                entity.getVersion(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDuplicates()
        );
        personAuditRepository.save(audit);
    }
}
