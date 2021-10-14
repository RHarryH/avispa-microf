package com.avispa.microf.model.invoice;

import com.avispa.microf.dto.InvoiceDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class InvoiceMapper {
    // not required when componentModel = "spring", can be autowired
    //InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @BeforeMapping
    protected void convertGroupingSeparator(InvoiceDto invoiceDto, @MappingTarget Invoice invoice) {
        // converts normal space to non-breaking space used as grouping separator
        // in Polish and French (and probably mode) locales
        if(null != invoiceDto.getNetValue()) {
            invoiceDto.setNetValue(invoiceDto.getNetValue().replace(' ', '\u00a0'));
        }
    }

    /*@AfterMapping
    protected void computeIndirectValues(InvoiceDto invoiceDto, @MappingTarget Invoice invoice) {
        invoice.computeIndirectValues();
    }*/

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "netValue", target= "netValue", numberFormat = "#,##0.00")
    public abstract InvoiceDto convertToDto(Invoice invoice);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "netValue", target= "netValue", numberFormat = "#,##0.00")
    public abstract Invoice convertToEntity(InvoiceDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "netValue", target= "netValue", numberFormat = "#,##0.00")
    public abstract void updateInvoiceFromDto(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);
}