package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CorporationAuditDto;
import com.techwave.paymentservice.dto.CorporationDto;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for corporation legal-entity operations.
 */
public interface CorporationService {

    CorporationDto createCorporation(CorporationDto dto);

    CorporationDto getCorporationById(UUID uuid);

    CorporationDto updateCorporation(UUID uuid,
                                     CorporationDto dto);

    List<CorporationAuditDto> getAuditTrail(UUID uuid);

    /**
     * Retrieves a corporation by country and company code.
     *
     * @param country the ISO 3166-1 Alpha-2 country code
     * @param code    the company code
     * @return the corporation DTO
     */
    CorporationDto getCorporationByCode(String country,
                                        String code);
}

