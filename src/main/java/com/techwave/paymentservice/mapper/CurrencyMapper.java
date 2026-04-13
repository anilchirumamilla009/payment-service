package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.entity.CurrencyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(target = "resourceType", constant = "currencies")
    CurrencyResponse toResponse(CurrencyEntity entity);
    
    List<CurrencyResponse> toResponses(List<CurrencyEntity> entities);
}
