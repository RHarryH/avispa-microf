package com.avispa.microf.model.invoice;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class InvoiceMapperIntegrationTest {
    private final InvoiceMapper mapper = Mappers.getMapper(InvoiceMapper.class);

    @Test
    void givenInvoiceToDto_whenMaps_thenCorrect() {
        Invoice invoice = new Invoice();
        invoice.setComments("Test comment");

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(invoice.getComments(), invoiceDto.getComments());
    }

    @Test
    void givenLocalDateToStringDate_whenMaps_thenCorrect() {
        Invoice invoice = new Invoice();
        invoice.setIssueDate(LocalDate.of(2011, 11, 10));
        invoice.setServiceDate(LocalDate.of(2011, 11, 10));

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(invoice.getIssueDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getIssueDate());
        assertEquals(invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getServiceDate());
    }

    @Test
    void givenDtoToInvoice_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setComments("Test comment");

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getComments(), invoice.getComments());
    }

    @Test
    void givenStringDateToLocalDate_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setIssueDate("2011-11-10");
        invoiceDto.setServiceDate("2011-11-10");

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getIssueDate(), invoice.getIssueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(invoiceDto.getServiceDate(), invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}