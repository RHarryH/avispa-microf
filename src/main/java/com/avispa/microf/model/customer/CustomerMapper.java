package com.avispa.microf.model.customer;

import com.avispa.microf.model.base.mapper.MultiTypeEntityDtoMapper;
import com.avispa.microf.model.base.mapper.MultiTypeMapperRegistry;
import com.avispa.microf.model.customer.corporate.CorporateCustomerMapper;
import com.avispa.microf.model.customer.retail.RetailCustomerMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CustomerMapper extends MultiTypeEntityDtoMapper<Customer, CustomerDto, CustomerCommonDto> {
    @Autowired
    private CorporateCustomerMapper corporateCustomerMapper;

    @Autowired
    private RetailCustomerMapper retailCustomerMapper;

    @Override
    protected void registerMappers(MultiTypeMapperRegistry multiTypeMapperRegistry) {
        multiTypeMapperRegistry.registerMapper("CORPORATE", corporateCustomerMapper);
        multiTypeMapperRegistry.registerMapper("RETAIL", retailCustomerMapper);
    }

    @Override
    protected String getDiscriminator(Customer entity) {
        return entity.getType();
    }

    @Override
    protected String getDiscriminator(CustomerDto dto) {
        return dto.getType();
    }
}