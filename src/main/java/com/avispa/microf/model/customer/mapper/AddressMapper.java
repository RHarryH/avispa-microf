package com.avispa.microf.model.customer.mapper;

import com.avispa.microf.model.customer.Address;
import com.avispa.microf.model.customer.AddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface AddressMapper {
    Address convertAddressToDto(AddressDto dto);

    AddressDto convertDtoToAddress(Address address);

    void updateAddressFromDto(AddressDto addressDto, @MappingTarget Address address);
}