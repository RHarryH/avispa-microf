package com.avispa.microf.model.ui.widget.list.mapper;

import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.ui.widget.list.dto.ListDataDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class ListDataDtoMapperIntegrationTest {
    private final ListDataDtoMapper mapper = Mappers.getMapper(ListDataDtoMapper.class);

    @Test
    void givenCommonDto_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setObjectName("test name");
        invoiceDto.setIssueDate(LocalDate.of(2022, Month.JULY, 14).format(DateTimeFormatter.ISO_LOCAL_DATE));
        invoiceDto.setHasPdfRendition(true);

        ListDataDto listDataDto = mapper.convert(invoiceDto, List.of("objectName", "issueDate"));

        assertEquals(invoiceDto.getId(), listDataDto.getId());
        assertEquals(invoiceDto.hasPdfRendition(), listDataDto.hasPdfRendition());
        assertEquals(List.of(invoiceDto.getObjectName(), invoiceDto.getIssueDate()), listDataDto.getValues());
    }
}