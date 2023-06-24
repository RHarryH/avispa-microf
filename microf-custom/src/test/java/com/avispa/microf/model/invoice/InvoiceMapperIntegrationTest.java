/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.microf.model.invoice;

import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.payment.PaymentDto;
import com.avispa.ecm.model.EcmEntityRepository;
import com.avispa.ecm.model.configuration.dictionary.DictionaryValue;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.bankaccount.BankAccountRepository;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.invoice.payment.PaymentMapperImpl;
import com.avispa.microf.model.invoice.position.PositionMapperImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        PositionMapperImpl.class,
        PaymentMapperImpl.class
})
class InvoiceMapperIntegrationTest {
    @Autowired
    @InjectMocks
    private InvoiceMapperImpl invoiceMapper;

    @MockBean
    private EcmEntityRepository<DictionaryValue> dictionaryValueRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    public void init() {
        BankAccount bankAccount = getBankAccount();

        when(bankAccountRepository.getReferenceById(any(UUID.class))).thenReturn(bankAccount);
    }

    @Test
    void givenInvoiceToDto_whenMaps_thenCorrect() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());

        Invoice invoice = new Invoice();
        invoice.setSeller(customer);
        invoice.setBuyer(customer);
        invoice.setComments("Test comment");

        Payment payment = new Payment();
        payment.setBankAccount(getBankAccount());
        invoice.setPayment(payment);

        InvoiceDto invoiceDto = invoiceMapper.convertToDto(invoice);

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

        Payment payment = new Payment();
        payment.setBankAccount(getBankAccount());
        invoice.setPayment(payment);

        InvoiceDto invoiceDto = invoiceMapper.convertToDto(invoice);

        assertEquals(invoice.getIssueDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getIssueDate());
        assertEquals(invoice.getServiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE), invoiceDto.getServiceDate());
    }

    @Test
    void givenDtoToInvoice_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setSeller(UUID.randomUUID().toString());
        invoiceDto.setBuyer(UUID.randomUUID().toString());
        invoiceDto.setComments("Test comment");

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setBankAccount(UUID.randomUUID().toString());
        invoiceDto.setPayment(paymentDto);

        Invoice invoice = invoiceMapper.convertToEntity(invoiceDto);

        assertEquals(invoiceDto.getComments(), invoice.getComments());
    }

    @Test
    void givenStringDateToLocalDate_whenMaps_thenCorrect() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setSeller(UUID.randomUUID().toString());
        invoiceDto.setBuyer(UUID.randomUUID().toString());
        invoiceDto.setIssueDate("2011-11-10");
        invoiceDto.setServiceDate("2011-11-10");

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setBankAccount(UUID.randomUUID().toString());
        invoiceDto.setPayment(paymentDto);

        Invoice invoice = invoiceMapper.convertToEntity(invoiceDto);

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