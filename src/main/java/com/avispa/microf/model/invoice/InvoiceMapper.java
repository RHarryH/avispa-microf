package com.avispa.microf.model.invoice;

import com.avispa.microf.util.FormatUtils;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
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

    @Mapping(source = "netValue", target= "netValue", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    public abstract InvoiceDto convertToDto(Invoice invoice);

    @Mapping(source = "netValue", target= "netValue", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    public abstract Invoice convertToEntity(InvoiceDto dto);

    @Mapping(source = "netValue", target= "netValue", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    public abstract void updateInvoiceFromDto(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);
}