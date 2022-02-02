package com.avispa.microf.model.customer.mapper;

import com.avispa.microf.model.customer.Address;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerListDto;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;
import com.avispa.microf.model.customer.type.retail.RetailCustomer;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Slf4j
public abstract class CustomerListMapper {
    @Mapping(target = "customerName", source="objectName")
    @Mapping(target = "corporate", constant = "false")
    @Mapping(target = "address", source="address", qualifiedByName = "convertAddressToString")
    protected abstract CustomerListDto toCustomerListDto(RetailCustomer customer);

    @Mapping(target = "customerName", source="objectName")
    @Mapping(target = "corporate", constant = "true")
    @Mapping(target = "address", source="address", qualifiedByName = "convertAddressToString")
    protected abstract CustomerListDto toCustomerListDto(CorporateCustomer customer);

    public CustomerListDto toCustomerListDto(Customer customer) {
        if(customer instanceof RetailCustomer) {
            return toCustomerListDto((RetailCustomer) customer);
        } else if(customer instanceof CorporateCustomer) {
            return toCustomerListDto((CorporateCustomer) customer);
        } else {
            log.error("Unknown type of customer");
            return null;
        }
    }

    @Named("convertAddressToString")
    protected String convertAddressToString(Address address) {
        return address.getStreet() + ", " + address.getPlace() + " " + address.getZipCode();
    }
}