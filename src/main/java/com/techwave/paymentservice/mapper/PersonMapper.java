package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.PersonAuditDto;
import com.techwave.paymentservice.dto.PersonDto;
import com.techwave.paymentservice.entity.PersonAuditEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

/**
 * MapStruct mapper for converting between
 * {@link PersonEntity} and {@link PersonDto},
 * as well as {@link PersonAuditEntity} and {@link PersonAuditDto}.
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

    @Mapping(target = "resourceType", constant = "people")
    PersonDto toDto(PersonEntity entity);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "resourceType", ignore = true)
    PersonEntity toEntity(PersonDto dto);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resourceType", ignore = true)
    void updateEntity(PersonDto dto,
                      @MappingTarget PersonEntity entity);

    @Mapping(target = "resourceType", constant = "person-audits")
    PersonAuditDto toAuditDto(PersonAuditEntity entity);

    List<PersonAuditDto> toAuditDtoList(
            List<PersonAuditEntity> entities);
}

