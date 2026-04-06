package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.SiloDto;
import com.techwave.paymentservice.entity.SiloEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * MapStruct mapper for converting between
 * {@link SiloEntity} and {@link SiloDto}.
 */
@Mapper(componentModel = "spring")
public interface SiloMapper {

    @Mapping(target = "type",
             expression = "java(entity.getType() != null"
                 + " ? entity.getType().name() : null)")
    SiloDto toDto(SiloEntity entity);

    List<SiloDto> toDtoList(List<SiloEntity> entities);
}

