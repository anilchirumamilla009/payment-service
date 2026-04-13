package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.request.CorporationRequest;
import com.techwave.paymentservice.dto.response.CorporationResponse;
import com.techwave.paymentservice.dto.response.CorporationAuditResponse;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.CorporationAudit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CorporationMapper {
    
    @Mapping(target = "resourceType", constant = "corporations")
    CorporationResponse toResponse(CorporationEntity entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    CorporationEntity toEntity(CorporationRequest request);
    
    @Mapping(target = "resourceType", constant = "corporation-audits")
    CorporationAuditResponse toAuditResponse(CorporationAudit audit);
    
    List<CorporationAuditResponse> toAuditResponses(List<CorporationAudit> audits);
}
