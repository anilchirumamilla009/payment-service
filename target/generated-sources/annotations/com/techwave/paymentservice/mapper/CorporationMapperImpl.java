package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.request.CorporationRequest;
import com.techwave.paymentservice.dto.response.CorporationAuditResponse;
import com.techwave.paymentservice.dto.response.CorporationResponse;
import com.techwave.paymentservice.entity.CorporationAudit;
import com.techwave.paymentservice.entity.CorporationEntity;
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
public class CorporationMapperImpl implements CorporationMapper {

    @Override
    public CorporationResponse toResponse(CorporationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CorporationResponse corporationResponse = new CorporationResponse();

        corporationResponse.setId( entity.getId() );
        corporationResponse.setName( entity.getName() );
        corporationResponse.setCode( entity.getCode() );
        corporationResponse.setIncorporationDate( entity.getIncorporationDate() );
        corporationResponse.setIncorporationCountry( entity.getIncorporationCountry() );
        corporationResponse.setType( entity.getType() );
        corporationResponse.setDuplicates( entity.getDuplicates() );

        corporationResponse.setResourceType( "corporations" );

        return corporationResponse;
    }

    @Override
    public CorporationEntity toEntity(CorporationRequest request) {
        if ( request == null ) {
            return null;
        }

        CorporationEntity corporationEntity = new CorporationEntity();

        corporationEntity.setName( request.getName() );
        corporationEntity.setCode( request.getCode() );
        corporationEntity.setIncorporationDate( request.getIncorporationDate() );
        corporationEntity.setIncorporationCountry( request.getIncorporationCountry() );
        corporationEntity.setType( request.getType() );
        corporationEntity.setDuplicates( request.getDuplicates() );

        return corporationEntity;
    }

    @Override
    public CorporationAuditResponse toAuditResponse(CorporationAudit audit) {
        if ( audit == null ) {
            return null;
        }

        CorporationAuditResponse corporationAuditResponse = new CorporationAuditResponse();

        corporationAuditResponse.setResource( audit.getResource() );
        corporationAuditResponse.setVersion( audit.getVersion() );
        corporationAuditResponse.setName( audit.getName() );
        corporationAuditResponse.setCode( audit.getCode() );
        corporationAuditResponse.setIncorporationDate( audit.getIncorporationDate() );
        corporationAuditResponse.setIncorporationCountry( audit.getIncorporationCountry() );
        corporationAuditResponse.setType( audit.getType() );
        corporationAuditResponse.setDuplicates( audit.getDuplicates() );

        corporationAuditResponse.setResourceType( "corporation-audits" );

        return corporationAuditResponse;
    }

    @Override
    public List<CorporationAuditResponse> toAuditResponses(List<CorporationAudit> audits) {
        if ( audits == null ) {
            return null;
        }

        List<CorporationAuditResponse> list = new ArrayList<CorporationAuditResponse>( audits.size() );
        for ( CorporationAudit corporationAudit : audits ) {
            list.add( toAuditResponse( corporationAudit ) );
        }

        return list;
    }
}
