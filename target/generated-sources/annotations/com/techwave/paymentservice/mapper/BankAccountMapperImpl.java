package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.request.BankAccountRequest;
import com.techwave.paymentservice.dto.response.BankAccountAuditResponse;
import com.techwave.paymentservice.dto.response.BankAccountResponse;
import com.techwave.paymentservice.entity.BankAccountAudit;
import com.techwave.paymentservice.entity.BankAccountEntity;
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
public class BankAccountMapperImpl implements BankAccountMapper {

    @Override
    public BankAccountResponse toResponse(BankAccountEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BankAccountResponse bankAccountResponse = new BankAccountResponse();

        bankAccountResponse.setId( entity.getId() );
        bankAccountResponse.setBeneficiary( entity.getBeneficiary() );
        bankAccountResponse.setBeneficiaryAddress( entity.getBeneficiaryAddress() );
        bankAccountResponse.setNickname( entity.getNickname() );
        bankAccountResponse.setIban( entity.getIban() );
        bankAccountResponse.setBic( entity.getBic() );
        bankAccountResponse.setAccountNumber( entity.getAccountNumber() );
        bankAccountResponse.setNationalBankCode( entity.getNationalBankCode() );
        bankAccountResponse.setNationalBranchCode( entity.getNationalBranchCode() );
        bankAccountResponse.setNationalClearingCode( entity.getNationalClearingCode() );
        bankAccountResponse.setCurrency( entity.getCurrency() );
        bankAccountResponse.setCountry( entity.getCountry() );

        bankAccountResponse.setResourceType( "bank-accounts" );

        return bankAccountResponse;
    }

    @Override
    public BankAccountEntity toEntity(BankAccountRequest request) {
        if ( request == null ) {
            return null;
        }

        BankAccountEntity bankAccountEntity = new BankAccountEntity();

        bankAccountEntity.setBeneficiary( request.getBeneficiary() );
        bankAccountEntity.setBeneficiaryAddress( request.getBeneficiaryAddress() );
        bankAccountEntity.setNickname( request.getNickname() );
        bankAccountEntity.setIban( request.getIban() );
        bankAccountEntity.setBic( request.getBic() );
        bankAccountEntity.setAccountNumber( request.getAccountNumber() );
        bankAccountEntity.setNationalBankCode( request.getNationalBankCode() );
        bankAccountEntity.setNationalBranchCode( request.getNationalBranchCode() );
        bankAccountEntity.setNationalClearingCode( request.getNationalClearingCode() );
        bankAccountEntity.setCurrency( request.getCurrency() );
        bankAccountEntity.setCountry( request.getCountry() );

        return bankAccountEntity;
    }

    @Override
    public BankAccountAuditResponse toAuditResponse(BankAccountAudit audit) {
        if ( audit == null ) {
            return null;
        }

        BankAccountAuditResponse bankAccountAuditResponse = new BankAccountAuditResponse();

        bankAccountAuditResponse.setResource( audit.getResource() );
        bankAccountAuditResponse.setVersion( audit.getVersion() );
        bankAccountAuditResponse.setBeneficiary( audit.getBeneficiary() );
        bankAccountAuditResponse.setBeneficiaryAddress( audit.getBeneficiaryAddress() );
        bankAccountAuditResponse.setNickname( audit.getNickname() );
        bankAccountAuditResponse.setIban( audit.getIban() );
        bankAccountAuditResponse.setBic( audit.getBic() );
        bankAccountAuditResponse.setAccountNumber( audit.getAccountNumber() );
        bankAccountAuditResponse.setNationalBankCode( audit.getNationalBankCode() );
        bankAccountAuditResponse.setNationalBranchCode( audit.getNationalBranchCode() );
        bankAccountAuditResponse.setNationalClearingCode( audit.getNationalClearingCode() );
        bankAccountAuditResponse.setCurrency( audit.getCurrency() );
        bankAccountAuditResponse.setCountry( audit.getCountry() );

        bankAccountAuditResponse.setResourceType( "bank-account-audits" );

        return bankAccountAuditResponse;
    }

    @Override
    public List<BankAccountAuditResponse> toAuditResponses(List<BankAccountAudit> audits) {
        if ( audits == null ) {
            return null;
        }

        List<BankAccountAuditResponse> list = new ArrayList<BankAccountAuditResponse>( audits.size() );
        for ( BankAccountAudit bankAccountAudit : audits ) {
            list.add( toAuditResponse( bankAccountAudit ) );
        }

        return list;
    }
}
