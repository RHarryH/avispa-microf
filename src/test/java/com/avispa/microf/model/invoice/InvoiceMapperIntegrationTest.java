package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.EcmEntityRepository;
import com.avispa.ecm.model.configuration.dictionary.DictionaryValue;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.bankaccount.BankAccountRepository;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.invoice.position.PositionMapperImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    private EcmEntityRepository<DictionaryValue> dictionaryValueRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    public void init() {
        BankAccount bankAccount = getBankAccount();

        when(bankAccountRepository.getById(any(UUID.class))).thenReturn(bankAccount);
    }

    @Test
    void givenInvoiceToDto_whenMaps_thenCorrect() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());

        Invoice invoice = new Invoice();
        invoice.setSeller(customer);
        invoice.setBuyer(customer);
        invoice.setComments("Test comment");
        invoice.setBankAccount(getBankAccount());

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(invoice.getComments(), invoiceDto.getComments());
    }

    @Test
    void givenLocalDateToStringDate_whenMaps_thenCorrect() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());

        Invoice invoice = new Invoice();
        invoice.setSeller(customer);
        invoice.setBuyer(customer);
        invoice.setIssueDate(LocalDate.of(2011, 11, 10));
        invoice.setServiceDate(LocalDate.of(2011, 11, 10));
        invoice.setBankAccount(getBankAccount());

        InvoiceDto invoiceDto = mapper.convertToDto(invoice);

        assertEquals(invoice.getIssueDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getIssueDate());
        assertEquals(invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getServiceDate());
    }

    @Test
    void givenDtoToInvoice_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setSeller(UUID.randomUUID().toString());
        invoiceDto.setBuyer(UUID.randomUUID().toString());
        invoiceDto.setComments("Test comment");
        invoiceDto.setBankAccount(UUID.randomUUID().toString());

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getComments(), invoice.getComments());
    }

    @Test
    void givenStringDateToLocalDate_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setSeller(UUID.randomUUID().toString());
        invoiceDto.setBuyer(UUID.randomUUID().toString());
        invoiceDto.setIssueDate("2011-11-10");
        invoiceDto.setServiceDate("2011-11-10");
        invoiceDto.setBankAccount(UUID.randomUUID().toString());

        Invoice invoice = mapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getIssueDate(), invoice.getIssueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(invoiceDto.getServiceDate(), invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    private BankAccount getBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.randomUUID());
        bankAccount.setAccountNumber(RandomStringUtils.randomAlphanumeric(24));
        bankAccount.setBankName("test bank");
        return bankAccount;
    }
}