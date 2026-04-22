package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.request.PersonRequest;
import com.techwave.paymentservice.dto.response.PersonAuditResponse;
import com.techwave.paymentservice.dto.response.PersonResponse;
import com.techwave.paymentservice.entity.PersonAudit;
import com.techwave.paymentservice.entity.PersonEntity;
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
public class PersonMapperImpl implements PersonMapper {

    @Override
    public PersonResponse toResponse(PersonEntity entity) {
        if ( entity == null ) {
            return null;
        }

        PersonResponse personResponse = new PersonResponse();

        personResponse.setId( entity.getId() );
        personResponse.setFirstName( entity.getFirstName() );
        personResponse.setLastName( entity.getLastName() );
        personResponse.setDuplicates( entity.getDuplicates() );

        personResponse.setResourceType( "people" );

        return personResponse;
    }

    @Override
    public PersonEntity toEntity(PersonRequest request) {
        if ( request == null ) {
            return null;
        }

        PersonEntity personEntity = new PersonEntity();

        personEntity.setFirstName( request.getFirstName() );
        personEntity.setLastName( request.getLastName() );
        personEntity.setDuplicates( request.getDuplicates() );

        return personEntity;
    }

    @Override
    public PersonAuditResponse toAuditResponse(PersonAudit audit) {
        if ( audit == null ) {
            return null;
        }

        PersonAuditResponse personAuditResponse = new PersonAuditResponse();

        personAuditResponse.setResource( audit.getResource() );
        personAuditResponse.setVersion( audit.getVersion() );
        personAuditResponse.setFirstName( audit.getFirstName() );
        personAuditResponse.setLastName( audit.getLastName() );
        personAuditResponse.setDuplicates( audit.getDuplicates() );

        personAuditResponse.setResourceType( "person-audits" );

        return personAuditResponse;
    }

    @Override
    public List<PersonAuditResponse> toAuditResponses(List<PersonAudit> audits) {
        if ( audits == null ) {
            return null;
        }

        List<PersonAuditResponse> list = new ArrayList<PersonAuditResponse>( audits.size() );
        for ( PersonAudit personAudit : audits ) {
            list.add( toAuditResponse( personAudit ) );
        }

        return list;
    }
}
