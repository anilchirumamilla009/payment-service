package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.SiloResponse;
import com.techwave.paymentservice.entity.SiloEntity;
import com.techwave.paymentservice.entity.SiloType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SiloMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(target = "resourceType", constant = "silos")
    @Mapping(source = "type", target = "type")
    SiloResponse toResponse(SiloEntity entity);
    
    List<SiloResponse> toResponses(List<SiloEntity> entities);
    
    default String mapSiloType(SiloType type) {
        return type != null ? type.name() : null;
    }
}
