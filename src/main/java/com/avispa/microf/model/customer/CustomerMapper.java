package com.avispa.microf.model.customer;

import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomerDto;
import com.avispa.microf.model.customer.type.retail.RetailCustomer;
import com.avispa.microf.model.customer.type.retail.RetailCustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    RetailCustomerDto convertToDto(RetailCustomer customer);

    CorporateCustomerDto convertToDto(CorporateCustomer customer);

    @Mapping(target = "address", source=".", qualifiedByName = "convertAddressToEntity")
    RetailCustomer convertToEntity(RetailCustomerDto dto);

    @Mapping(target = "address", source=".", qualifiedByName = "convertAddressToEntity")
    CorporateCustomer convertToEntity(CorporateCustomerDto dto);

    Address convertAddressToEntity(CustomerDto dto);

    //void updateCustomerFromDto(CustomerDto customerDto, @MappingTarget Customer customer);
}