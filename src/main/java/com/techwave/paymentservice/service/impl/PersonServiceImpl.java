package com.techwave.paymentservice.service.impl;

import com.techwave.paymentservice.dto.PersonAuditDto;
import com.techwave.paymentservice.dto.PersonDto;
import com.techwave.paymentservice.entity.PersonAuditEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.PersonMapper;
import com.techwave.paymentservice.repository.PersonAuditRepository;
import com.techwave.paymentservice.repository.PersonRepository;
import com.techwave.paymentservice.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link PersonService}.
 * Manages person lifecycle including creation, retrieval,
 * update, and audit trail tracking.
 */
@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private static final Logger log =
            LoggerFactory.getLogger(PersonServiceImpl.class);

    private static final String RESOURCE_TYPE = "Person";

    private final PersonRepository personRepository;
    private final PersonAuditRepository personAuditRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(
            PersonRepository personRepository,
            PersonAuditRepository personAuditRepository,
            PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personAuditRepository = personAuditRepository;
        this.personMapper = personMapper;
    }

    @Override
    @Transactional
    public PersonDto createPerson(PersonDto personDto) {
        log.debug("Creating person: {} {}",
                personDto.getFirstName(),
                personDto.getLastName());
        PersonEntity entity = personMapper.toEntity(personDto);
        entity.setId(UUID.randomUUID());
        entity.setVersion(1);

        PersonEntity saved = personRepository.save(entity);
        createAuditRecord(saved);

        log.info("Person created with id: {}", saved.getId());
        return personMapper.toDto(saved);
    }

    @Override
    public PersonDto getPersonById(UUID uuid) {
        log.debug("Fetching person with id: {}", uuid);
        return personRepository.findById(uuid)
                .map(personMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_TYPE, uuid.toString()));
    }

    @Override
    @Transactional
    public PersonDto updatePerson(UUID uuid,
                                  PersonDto personDto) {
        log.debug("Updating person with id: {}", uuid);
        PersonEntity entity = personRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_TYPE, uuid.toString()));

        personMapper.updateEntity(personDto, entity);

        PersonEntity saved = personRepository.saveAndFlush(entity);
        createAuditRecord(saved);

        log.info("Person updated with id: {}, version: {}",
                saved.getId(), saved.getVersion());
        return personMapper.toDto(saved);
    }

    @Override
    public List<PersonAuditDto> getAuditTrail(UUID uuid) {
        log.debug("Fetching audit trail for person: {}", uuid);
        if (!personRepository.existsById(uuid)) {
            throw new ResourceNotFoundException(
                    RESOURCE_TYPE, uuid.toString());
        }
        List<PersonAuditEntity> audits =
                personAuditRepository
                        .findByResourceOrderByVersionAsc(uuid);
        return personMapper.toAuditDtoList(audits);
    }

    /**
     * Creates an audit trail record capturing the current
     * state of the person entity.
     */
    private void createAuditRecord(PersonEntity entity) {
        PersonAuditEntity audit = new PersonAuditEntity();
        audit.setResource(entity.getId());
        audit.setVersion(entity.getVersion());
        audit.setFirstName(entity.getFirstName());
        audit.setLastName(entity.getLastName());
        audit.setDuplicates(entity.getDuplicates());
        personAuditRepository.save(audit);
        log.debug("Audit record created for person: {}, "
                + "version: {}",
                entity.getId(), entity.getVersion());
    }
}
