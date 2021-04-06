package com.avispa.microf.model.invoice;

import com.avispa.microf.dto.InvoiceDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateInvoiceFromDto(InvoiceDto dto, @MappingTarget Invoice entity);
}