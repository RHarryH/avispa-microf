package com.avispa.microf.model.customer.mapper;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomerDto;
import com.avispa.microf.model.customer.type.retail.RetailCustomer;
import com.avispa.microf.model.customer.type.retail.RetailCustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = AddressMapper.class)
@Slf4j
public abstract class CustomerMapper {
    protected abstract RetailCustomerDto convertToDto(RetailCustomer customer);

    protected abstract CorporateCustomerDto convertToDto(CorporateCustomer customer);

    public CustomerDto convertToDto(Customer customer) {
        if(customer instanceof RetailCustomer) {
            return convertToDto((RetailCustomer) customer);
        } else if(customer instanceof CorporateCustomer) {
            return convertToDto((CorporateCustomer) customer);
        } else {
            log.error("Unknown type of customer");
            return null;
        }
    }

    public abstract RetailCustomer convertToEntity(RetailCustomerDto dto);

    public abstract CorporateCustomer convertToEntity(CorporateCustomerDto dto);

    protected abstract void updateCustomerFromDto(RetailCustomerDto customerDto, @MappingTarget RetailCustomer customer);

    protected abstract void updateCustomerFromDto(CorporateCustomerDto customerDto, @MappingTarget CorporateCustomer customer);

    public void updateCustomerFromDto(CustomerDto customerDto, @MappingTarget Customer customer) {
        if(customer instanceof RetailCustomer) {
            updateCustomerFromDto((RetailCustomerDto) customerDto, (RetailCustomer) customer);
        } else if(customer instanceof CorporateCustomer) {
            updateCustomerFromDto((CorporateCustomerDto) customerDto, (CorporateCustomer) customer);
        } else {
            log.error("Unknown type of customer");
        }
    }

}