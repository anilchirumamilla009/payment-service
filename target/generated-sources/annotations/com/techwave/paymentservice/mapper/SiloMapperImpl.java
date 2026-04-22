package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.SiloResponse;
import com.techwave.paymentservice.entity.SiloEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-21T10:55:29+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class SiloMapperImpl implements SiloMapper {

    @Override
    public SiloResponse toResponse(SiloEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SiloResponse siloResponse = new SiloResponse();

        siloResponse.setId( entity.getId() );
        siloResponse.setType( mapSiloType( entity.getType() ) );
        siloResponse.setName( entity.getName() );
        siloResponse.setDescription( entity.getDescription() );
        siloResponse.setEmail( entity.getEmail() );
        siloResponse.setDefaultBaseCurrency( entity.getDefaultBaseCurrency() );
        siloResponse.setDefaultCreditLimit( entity.getDefaultCreditLimit() );
        siloResponse.setDefaultProfitShare( entity.getDefaultProfitShare() );

        siloResponse.setResourceType( "silos" );

        return siloResponse;
    }

    @Override
    public List<SiloResponse> toResponses(List<SiloEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SiloResponse> list = new ArrayList<SiloResponse>( entities.size() );
        for ( SiloEntity siloEntity : entities ) {
            list.add( toResponse( siloEntity ) );
        }

        return list;
    }
}
