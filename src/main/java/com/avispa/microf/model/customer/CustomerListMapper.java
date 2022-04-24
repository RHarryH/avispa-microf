package com.avispa.microf.model.customer;

import com.avispa.microf.model.customer.address.Address;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Slf4j
public abstract class CustomerListMapper {
    @Mapping(target = "customerName", source="objectName")
    @Mapping(target = "corporate", source="type", qualifiedByName = "isCorporate")
    @Mapping(target = "address", source="address", qualifiedByName = "convertAddressToString")
    protected abstract CustomerListDto toCustomerListDto(Customer customer);

    @Named("isCorporate")
    protected boolean isCorporate(String type) {
        return type.equals("CORPORATE");
    }

    @Named("convertAddressToString")
    protected String convertAddressToString(Address address) {
        return address.getStreet() + ", " + address.getPlace() + " " + address.getZipCode();
    }
}