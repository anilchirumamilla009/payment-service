package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.CountryResponse;
import com.techwave.paymentservice.entity.CountryEntity;
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
public class CountryMapperImpl implements CountryMapper {

    @Override
    public CountryResponse toResponse(CountryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CountryResponse countryResponse = new CountryResponse();

        countryResponse.setId( entity.getId() );
        countryResponse.setName( entity.getName() );
        countryResponse.setNumericCode( entity.getNumericCode() );
        countryResponse.setAlpha3Code( entity.getAlpha3Code() );
        countryResponse.setEurozone( entity.isEurozone() );
        countryResponse.setSepa( entity.isSepa() );

        countryResponse.setResourceType( "countries" );

        return countryResponse;
    }

    @Override
    public List<CountryResponse> toResponses(List<CountryEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CountryResponse> list = new ArrayList<CountryResponse>( entities.size() );
        for ( CountryEntity countryEntity : entities ) {
            list.add( toResponse( countryEntity ) );
        }

        return list;
    }
}
