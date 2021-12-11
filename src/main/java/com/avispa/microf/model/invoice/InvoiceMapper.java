package com.avispa.microf.model.invoice;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.util.FormatUtils;
import org.hibernate.Hibernate;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class InvoiceMapper {
    // not required when componentModel = "spring", can be autowired
    //InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeMapping
    protected void convertGroupingSeparator(InvoiceDto invoiceDto, @MappingTarget Invoice invoice) {
        // converts normal space to non-breaking space used as grouping separator
        // in Polish and French (and probably mode) locales
        if(null != invoiceDto.getNetValue()) {
            invoiceDto.setNetValue(invoiceDto.getNetValue().replace(' ', '\u00a0'));
        }
    }

    @Mapping(source = "netValue", target = "netValue", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    public abstract InvoiceDto convertToDto(Invoice invoice);

    protected UUID customerToId(Customer customer) {
        return customer.getId();
    }

    @Mapping(source = "netValue", target= "netValue", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    public abstract Invoice convertToEntity(InvoiceDto dto);

    protected Customer idToCustomer(UUID customerId) {
        return Hibernate.unproxy(customerRepository.getOne(customerId), Customer.class);
    }

    @Mapping(source = "netValue", target= "netValue", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    public abstract void updateInvoiceFromDto(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);
}