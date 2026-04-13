package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.CurrencyResponse;
import com.techwave.paymentservice.entity.CurrencyEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-10T14:50:39+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CurrencyMapperImpl implements CurrencyMapper {

    @Override
    public CurrencyResponse toResponse(CurrencyEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CurrencyResponse currencyResponse = new CurrencyResponse();

        currencyResponse.setId( entity.getId() );
        currencyResponse.setName( entity.getName() );

        currencyResponse.setResourceType( "currencies" );

        return currencyResponse;
    }

    @Override
    public List<CurrencyResponse> toResponses(List<CurrencyEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CurrencyResponse> list = new ArrayList<CurrencyResponse>( entities.size() );
        for ( CurrencyEntity currencyEntity : entities ) {
            list.add( toResponse( currencyEntity ) );
        }

        return list;
    }
}
