package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.PersonAuditDto;
import com.techwave.paymentservice.dto.PersonDto;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for person legal-entity operations.
 */
public interface PersonService {

    /**
     * Creates a new person entity.
     *
     * @param personDto the person data
     * @return the created person DTO with generated UUID
     */
    PersonDto createPerson(PersonDto personDto);

    /**
     * Retrieves a person by UUID.
     *
     * @param uuid the person's unique identifier
     * @return the person DTO
     */
    PersonDto getPersonById(UUID uuid);

    /**
     * Updates an existing person entity (partial update).
     *
     * @param uuid      the person's unique identifier
     * @param personDto the fields to update
     * @return the updated person DTO
     */
    PersonDto updatePerson(UUID uuid, PersonDto personDto);

    /**
     * Retrieves the full audit trail for a person.
     *
     * @param uuid the person's unique identifier
     * @return list of audit trail DTOs ordered by version
     */
    List<PersonAuditDto> getAuditTrail(UUID uuid);
}

