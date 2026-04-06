package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.BankAccountAuditDto;
import com.techwave.paymentservice.dto.BankAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.entity.BankAccountAuditEntity;
import com.techwave.paymentservice.entity.BankAccountEntity;
import com.techwave.paymentservice.entity.LegalEntityBase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;
import java.util.Set;

/**
 * MapStruct mapper for converting between
 * {@link BankAccountEntity} and {@link BankAccountDto},
 * as well as {@link BankAccountAuditEntity} and
 * {@link BankAccountAuditDto}.
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
public interface BankAccountMapper {

    @Mapping(target = "resourceType",
             constant = "bank-accounts")
    BankAccountDto toDto(BankAccountEntity entity);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "beneficialOwners", ignore = true)
    BankAccountEntity toEntity(BankAccountDto dto);

    @Mapping(target = "resourceType",
             constant = "bank-account-audits")
    BankAccountAuditDto toAuditDto(BankAccountAuditEntity entity);

    List<BankAccountAuditDto> toAuditDtoList(
            List<BankAccountAuditEntity> entities);

    /**
     * Maps a legal entity base to a lightweight DTO
     * containing only id and resourceType.
     */
    @Mapping(target = "resourceType",
             source = "resourceType")
    LegalEntityDto toLegalEntityDto(LegalEntityBase entity);

    List<LegalEntityDto> toLegalEntityDtoList(
            Set<LegalEntityBase> entities);
}

