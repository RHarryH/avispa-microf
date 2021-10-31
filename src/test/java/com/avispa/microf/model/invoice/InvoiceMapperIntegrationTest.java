package com.avispa.microf.model.invoice;

import com.avispa.microf.util.FormatUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

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
    void givenBigDecimalToString_whenMaps_thenCorrect() {
        Invoice invoice = new Invoice();
        invoice.setNetValue(new BigDecimal("1000.24"));

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(FormatUtils.format(invoice.getNetValue()), invoiceDto.getNetValue());
    }

    @Test
    void givenLocalDateToStringDate_whenMaps_thenCorrect() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.of(2011, 11, 10));
        invoice.setServiceDate(LocalDate.of(2011, 11, 10));

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(invoice.getInvoiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getInvoiceDate());
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
    void givenStringToBigDecimal_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setNetValue("1 000,24");

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getNetValue(), FormatUtils.format(invoice.getNetValue()));
    }

    @Test
    void givenStringDateToLocalDate_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceDate("2011-11-10");
        invoiceDto.setServiceDate("2011-11-10");

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getInvoiceDate(), invoice.getInvoiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(invoiceDto.getServiceDate(), invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}