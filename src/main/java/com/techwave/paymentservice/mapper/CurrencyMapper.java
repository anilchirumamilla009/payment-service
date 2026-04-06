package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.CurrencyDto;
import com.techwave.paymentservice.entity.CurrencyEntity;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * MapStruct mapper for converting between
 * {@link CurrencyEntity} and {@link CurrencyDto}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto toDto(CurrencyEntity entity);

    List<CurrencyDto> toDtoList(List<CurrencyEntity> entities);

    CurrencyEntity toEntity(CurrencyDto dto);
}

