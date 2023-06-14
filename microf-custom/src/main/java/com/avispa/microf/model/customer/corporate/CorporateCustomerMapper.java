package com.avispa.microf.model.customer.corporate;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.address.AddressMapper;
import com.avispa.ecm.model.base.mapper.IEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = AddressMapper.class)
public interface CorporateCustomerMapper extends IEntityDtoMapper<Customer, CorporateCustomerDto> {
}