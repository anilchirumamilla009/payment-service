package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.CorporationAuditDto;
import com.techwave.paymentservice.dto.CorporationDto;
import com.techwave.paymentservice.entity.CorporationAuditEntity;
import com.techwave.paymentservice.entity.CorporationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

/**
 * MapStruct mapper for converting between
 * {@link CorporationEntity} and {@link CorporationDto},
 * as well as {@link CorporationAuditEntity} and
 * {@link CorporationAuditDto}.
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
public interface CorporationMapper {

    @Mapping(target = "resourceType", constant = "corporations")
    CorporationDto toDto(CorporationEntity entity);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "resourceType", ignore = true)
    CorporationEntity toEntity(CorporationDto dto);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resourceType", ignore = true)
    void updateEntity(CorporationDto dto,
                      @MappingTarget CorporationEntity entity);

    @Mapping(target = "resourceType",
             constant = "corporation-audits")
    CorporationAuditDto toAuditDto(CorporationAuditEntity entity);

    List<CorporationAuditDto> toAuditDtoList(
            List<CorporationAuditEntity> entities);
}

