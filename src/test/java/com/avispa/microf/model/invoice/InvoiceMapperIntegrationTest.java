package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.dictionary.DictionaryValue;
import com.avispa.ecm.model.configuration.dictionary.DictionaryValueMapper;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;
import com.avispa.microf.model.invoice.position.PositionMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        InvoiceMapperImpl.class,
        PositionMapperImpl.class
})
class InvoiceMapperIntegrationTest {
    @SpyBean
    private InvoiceMapperImpl mapper;

    @MockBean
    private EcmObjectRepository<DictionaryValue> ecmObjectRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private DictionaryValueMapper dictionaryValueMapper;

    @Test
    void givenInvoiceToDto_whenMaps_thenCorrect() {
        Invoice invoice = new Invoice();
        invoice.setSeller(new CorporateCustomer());
        invoice.setBuyer(new CorporateCustomer());
        invoice.setComments("Test comment");

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(invoice.getComments(), invoiceDto.getComments());
    }

    @Test
    void givenLocalDateToStringDate_whenMaps_thenCorrect() {
        Invoice invoice = new Invoice();
        invoice.setSeller(new CorporateCustomer());
        invoice.setBuyer(new CorporateCustomer());
        invoice.setIssueDate(LocalDate.of(2011, 11, 10));
        invoice.setServiceDate(LocalDate.of(2011, 11, 10));

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(invoice.getIssueDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getIssueDate());
        assertEquals(invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getServiceDate());
    }

    @Test
    void givenDtoToInvoice_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setSeller(UUID.randomUUID());
        invoiceDto.setBuyer(UUID.randomUUID());
        invoiceDto.setComments("Test comment");

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getComments(), invoice.getComments());
    }

    @Test
    void givenStringDateToLocalDate_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setSeller(UUID.randomUUID());
        invoiceDto.setBuyer(UUID.randomUUID());
        invoiceDto.setIssueDate("2011-11-10");
        invoiceDto.setServiceDate("2011-11-10");

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getIssueDate(), invoice.getIssueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(invoiceDto.getServiceDate(), invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}