package com.avispa.microf.model.customer;

import com.avispa.microf.model.base.IEntityDtoMapper;
import com.avispa.microf.model.customer.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = AddressMapper.class)
public interface CustomerMapper extends IEntityDtoMapper<Customer, CustomerDto> {
}