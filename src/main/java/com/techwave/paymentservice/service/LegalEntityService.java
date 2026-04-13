package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.request.CorporationRequest;
import com.techwave.paymentservice.dto.request.PersonRequest;
import com.techwave.paymentservice.dto.response.CorporationAuditResponse;
import com.techwave.paymentservice.dto.response.CorporationResponse;
import com.techwave.paymentservice.dto.response.PersonAuditResponse;
import com.techwave.paymentservice.dto.response.PersonResponse;
import java.util.List;
import java.util.UUID;

public interface LegalEntityService {
    
    // Corporations
    CorporationResponse createCorporation(CorporationRequest request);
    CorporationResponse getCorporation(UUID id);
    CorporationResponse updateCorporation(UUID id, CorporationRequest request);
    CorporationResponse getCorporationByCode(String country, String code);
    List<CorporationAuditResponse> getCorporationAuditTrail(UUID id);
    
    // People
    PersonResponse createPerson(PersonRequest request);
    PersonResponse getPerson(UUID id);
    PersonResponse updatePerson(UUID id, PersonRequest request);
    List<PersonAuditResponse> getPersonAuditTrail(UUID id);
}
