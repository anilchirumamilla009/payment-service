package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(target = "resourceType", constant = "countries")
    CountryResponse toResponse(CountryEntity entity);
    
    List<CountryResponse> toResponses(List<CountryEntity> entities);
}
