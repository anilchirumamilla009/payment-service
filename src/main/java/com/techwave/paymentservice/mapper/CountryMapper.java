package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.CountryDto;
import com.techwave.paymentservice.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

/**
 * MapStruct mapper for converting between
 * {@link CountryEntity} and {@link CountryDto}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryDto toDto(CountryEntity entity);

    List<CountryDto> toDtoList(List<CountryEntity> entities);

    CountryEntity toEntity(CountryDto dto);

    void updateEntity(CountryDto dto,
                      @MappingTarget CountryEntity entity);
}

