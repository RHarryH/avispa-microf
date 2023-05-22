package com.avispa.microf.model.customer.address;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    Address convertToDto(AddressDto dto);

    AddressDto convertDtoToEntity(Address address);

    void updateEntityFromDto(AddressDto addressDto, @MappingTarget Address address);
}