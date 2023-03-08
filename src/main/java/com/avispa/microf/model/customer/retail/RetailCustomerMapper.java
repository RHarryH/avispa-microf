package com.avispa.microf.model.customer.retail;

import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = AddressMapper.class)
public interface RetailCustomerMapper extends IEntityDtoMapper<Customer, RetailCustomerDto> {
}