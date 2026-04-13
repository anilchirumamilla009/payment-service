package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.request.BankAccountRequest;
import com.techwave.paymentservice.dto.response.BankAccountResponse;
import com.techwave.paymentservice.dto.response.BankAccountAuditResponse;
import com.techwave.paymentservice.entity.BankAccountEntity;
import com.techwave.paymentservice.entity.BankAccountAudit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    
    @Mapping(target = "resourceType", constant = "bank-accounts")
    BankAccountResponse toResponse(BankAccountEntity entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "beneficialOwners", ignore = true)
    BankAccountEntity toEntity(BankAccountRequest request);
    
    @Mapping(target = "resourceType", constant = "bank-account-audits")
    BankAccountAuditResponse toAuditResponse(BankAccountAudit audit);
    
    List<BankAccountAuditResponse> toAuditResponses(List<BankAccountAudit> audits);
}
