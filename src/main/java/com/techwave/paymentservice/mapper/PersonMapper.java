package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.request.PersonRequest;
import com.techwave.paymentservice.dto.response.PersonResponse;
import com.techwave.paymentservice.dto.response.PersonAuditResponse;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.entity.PersonAudit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    
    @Mapping(target = "resourceType", constant = "people")
    PersonResponse toResponse(PersonEntity entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    PersonEntity toEntity(PersonRequest request);
    
    @Mapping(target = "resourceType", constant = "person-audits")
    PersonAuditResponse toAuditResponse(PersonAudit audit);
    
    List<PersonAuditResponse> toAuditResponses(List<PersonAudit> audits);
}
