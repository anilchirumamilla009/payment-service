package com.techwave.paymentservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.BadRequestException;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.EntityMapper;
import com.techwave.paymentservice.model.Corporation;
import com.techwave.paymentservice.model.CorporationAudit;
import com.techwave.paymentservice.model.Person;
import com.techwave.paymentservice.model.PersonAudit;
import com.techwave.paymentservice.repository.CorporationAuditRepository;
import com.techwave.paymentservice.repository.CorporationRepository;
import com.techwave.paymentservice.repository.PersonAuditRepository;
import com.techwave.paymentservice.repository.PersonRepository;

/**
 * Business service for legal entity (corporation &amp; person) endpoints.
 */
@Service
public class LegalEntitiesService {

    private final CorporationRepository corporationRepository;
    private final CorporationAuditRepository corporationAuditRepository;
    private final PersonRepository personRepository;
    private final PersonAuditRepository personAuditRepository;
    private final EntityMapper mapper;

    public LegalEntitiesService(CorporationRepository corporationRepository,
                                CorporationAuditRepository corporationAuditRepository,
                                PersonRepository personRepository,
                                PersonAuditRepository personAuditRepository,
                                EntityMapper mapper) {
        this.corporationRepository = corporationRepository;
        this.corporationAuditRepository = corporationAuditRepository;
        this.personRepository = personRepository;
        this.personAuditRepository = personAuditRepository;
        this.mapper = mapper;
    }

    // ── Corporation ─────────────────────────────────────────────────────

    @Transactional
    public Corporation createCorporation(Corporation dto) {
        validateCorporation(dto);
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID());
        }
        CorporationEntity entity = mapper.toEntity(dto);
        entity = corporationRepository.save(entity);

        int nextVersion = corporationAuditRepository.countByResource(entity.getId()) + 1;
        corporationAuditRepository.save(mapper.toAuditEntity(entity, nextVersion));

        return mapper.toDto(entity);
    }

    public Corporation getCorporation(UUID uuid) {
        return corporationRepository.findById(uuid)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Corporation not found for id: " + uuid));
    }

    @Transactional
    public Corporation updateCorporation(UUID uuid, Corporation dto) {
        CorporationEntity existing = corporationRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Corporation not found for id: " + uuid));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getCode() != null) existing.setCode(dto.getCode());
        if (dto.getIncorporationDate() != null) existing.setIncorporationDate(dto.getIncorporationDate());
        if (dto.getIncorporationCountry() != null) existing.setIncorporationCountry(dto.getIncorporationCountry());
        if (dto.getType() != null) existing.setType(dto.getType());
        if (dto.getDuplicates() != null) existing.setDuplicates(dto.getDuplicates());

        existing = corporationRepository.save(existing);

        int nextVersion = corporationAuditRepository.countByResource(existing.getId()) + 1;
        corporationAuditRepository.save(mapper.toAuditEntity(existing, nextVersion));

        return mapper.toDto(existing);
    }

    public List<CorporationAudit> getCorporationAuditTrail(UUID uuid) {
        ensureCorporationExists(uuid);
        return corporationAuditRepository.findByResourceOrderByVersionAsc(uuid).stream()
                .map(mapper::toDto).toList();
    }

    public Corporation getCorporationByCode(String country, String code) {
        return corporationRepository.findByIncorporationCountryIgnoreCaseAndCodeIgnoreCase(country, code)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Corporation not found for country: " + country + " code: " + code));
    }

    // ── Person ──────────────────────────────────────────────────────────

    @Transactional
    public Person createPerson(Person dto) {
        validatePerson(dto);
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID());
        }
        PersonEntity entity = mapper.toEntity(dto);
        entity = personRepository.save(entity);

        int nextVersion = personAuditRepository.countByResource(entity.getId()) + 1;
        personAuditRepository.save(mapper.toAuditEntity(entity, nextVersion));

        return mapper.toDto(entity);
    }

    public Person getPerson(UUID uuid) {
        return personRepository.findById(uuid)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + uuid));
    }

    @Transactional
    public Person updatePerson(UUID uuid, Person dto) {
        PersonEntity existing = personRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + uuid));

        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getDuplicates() != null) existing.setDuplicates(dto.getDuplicates());

        existing = personRepository.save(existing);

        int nextVersion = personAuditRepository.countByResource(existing.getId()) + 1;
        personAuditRepository.save(mapper.toAuditEntity(existing, nextVersion));

        return mapper.toDto(existing);
    }

    public List<PersonAudit> getPersonAuditTrail(UUID uuid) {
        ensurePersonExists(uuid);
        return personAuditRepository.findByResourceOrderByVersionAsc(uuid).stream()
                .map(mapper::toDto).toList();
    }

    // ── Helpers ─────────────────────────────────────────────────────────

    private void ensureCorporationExists(UUID uuid) {
        if (!corporationRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("Corporation not found for id: " + uuid);
        }
    }

    private void ensurePersonExists(UUID uuid) {
        if (!personRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("Person not found for id: " + uuid);
        }
    }

    private void validateCorporation(Corporation dto) {
        if (dto == null) throw new BadRequestException("Corporation payload is required.");
        if (isBlank(dto.getName())) throw new BadRequestException("Corporation name is required.");
    }

    private void validatePerson(Person dto) {
        if (dto == null) throw new BadRequestException("Person payload is required.");
        if (isBlank(dto.getFirstName())) throw new BadRequestException("Person firstName is required.");
        if (isBlank(dto.getLastName())) throw new BadRequestException("Person lastName is required.");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
