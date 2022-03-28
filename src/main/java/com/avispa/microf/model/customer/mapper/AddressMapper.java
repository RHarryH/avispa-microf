package com.avispa.microf.model.customer.mapper;

import com.avispa.microf.model.customer.Address;
import com.avispa.microf.model.customer.AddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    Address convertToDto(AddressDto dto);

    AddressDto convertDtoToEntity(Address address);

    void updateEntityFromDto(AddressDto addressDto, @MappingTarget Address address);
}