package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.CustomerAccountResponse;
import com.techwave.paymentservice.entity.CustomerAccountEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-10T14:50:38+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CustomerAccountMapperImpl implements CustomerAccountMapper {

    @Override
    public CustomerAccountResponse toResponse(CustomerAccountEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerAccountResponse customerAccountResponse = new CustomerAccountResponse();

        customerAccountResponse.setAccountType( mapAccountType( entity.getAccountType() ) );
        customerAccountResponse.setAccountState( mapAccountState( entity.getAccountState() ) );
        customerAccountResponse.setId( entity.getId() );
        customerAccountResponse.setName( entity.getName() );
        customerAccountResponse.setDescription( entity.getDescription() );
        customerAccountResponse.setAccountManager( entity.getAccountManager() );
        customerAccountResponse.setAccountCreationTime( entity.getAccountCreationTime() );
        customerAccountResponse.setSilo( entity.getSilo() );

        customerAccountResponse.setResourceType( "customer-accounts" );

        return customerAccountResponse;
    }
}
