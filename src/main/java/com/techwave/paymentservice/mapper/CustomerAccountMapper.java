package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.response.CustomerAccountResponse;
import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.entity.CustomerAccountType;
import com.techwave.paymentservice.entity.CustomerAccountState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerAccountMapper {
    
    @Mapping(target = "resourceType", constant = "customer-accounts")
    @Mapping(source = "accountType", target = "accountType")
    @Mapping(source = "accountState", target = "accountState")
    CustomerAccountResponse toResponse(CustomerAccountEntity entity);
    
    default String mapAccountType(CustomerAccountType type) {
        return type != null ? type.name() : null;
    }
    
    default String mapAccountState(CustomerAccountState state) {
        return state != null ? state.name() : null;
    }
}
