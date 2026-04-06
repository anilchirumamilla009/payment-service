package com.techwave.paymentservice.mapper;

import com.techwave.paymentservice.dto.CustomerAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.entity.LegalEntityBase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

/**
 * MapStruct mapper for converting between
 * {@link CustomerAccountEntity} and {@link CustomerAccountDto}.
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerAccountMapper {

    @Mapping(target = "resourceType",
             constant = "customer-accounts")
    @Mapping(target = "accountType",
             expression = "java(entity.getAccountType() != null"
                 + " ? entity.getAccountType().name() : null)")
    @Mapping(target = "accountState",
             expression = "java(entity.getAccountState() != null"
                 + " ? entity.getAccountState().name() : null)")
    @Mapping(target = "accountCreationTime",
             expression = "java(toOffsetDateTime("
                 + "entity.getAccountCreationTime()))")
    CustomerAccountDto toDto(CustomerAccountEntity entity);

    @Mapping(target = "resourceType",
             source = "resourceType")
    LegalEntityDto toLegalEntityDto(LegalEntityBase entity);

    List<LegalEntityDto> toLegalEntityDtoList(
            Set<LegalEntityBase> entities);

    /**
     * Converts a LocalDateTime to OffsetDateTime using UTC offset.
     *
     * @param ldt the LocalDateTime to convert
     * @return the corresponding OffsetDateTime, or null if input
     *         is null
     */
    default OffsetDateTime toOffsetDateTime(LocalDateTime ldt) {
        return ldt != null
                ? ldt.atOffset(ZoneOffset.UTC)
                : null;
    }
}

