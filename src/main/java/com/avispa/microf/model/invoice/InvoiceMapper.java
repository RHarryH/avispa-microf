package com.avispa.microf.model.invoice;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.invoice.position.PositionMapper;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = PositionMapper.class)
public abstract class InvoiceMapper {
    // not required when componentModel = "spring", can be autowired
    //InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Autowired
    private CustomerRepository customerRepository;

    public abstract InvoiceDto convertToDto(Invoice invoice);

    protected UUID customerToId(Customer customer) {
        return customer.getId();
    }

    public abstract Invoice convertToEntity(InvoiceDto dto);

    protected Customer idToCustomer(UUID customerId) {
        return Hibernate.unproxy(customerRepository.getOne(customerId), Customer.class);
    }

    public abstract void updateInvoiceFromDto(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);
}