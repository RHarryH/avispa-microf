package com.avispa.microf.model.customer.type.corporate;

import com.avispa.microf.model.base.IEntityDtoMapper;
import com.avispa.microf.model.customer.mapper.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = AddressMapper.class)
public interface CorporateCustomerMapper extends IEntityDtoMapper<CorporateCustomer, CorporateCustomerDto> {
}