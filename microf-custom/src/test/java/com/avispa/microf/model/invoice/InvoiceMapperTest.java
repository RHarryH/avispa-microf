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

import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.bankaccount.BankAccountRepository;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.payment.PaymentDto;
import com.avispa.microf.model.invoice.payment.PaymentMapperImpl;
import com.avispa.microf.model.invoice.position.PositionMapperImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
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
class InvoiceMapperTest {
    public static final String CUSTOMER_ID = "97ddd2fc-6cd4-4bfa-86bc-93d95e0a3a80";
    public static final String ACCOUNT_ID = "97ddd2fc-6cd4-4bfa-86bc-93d95e0a3a88";

    @Autowired
    private InvoiceMapperImpl mapper;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Test
    void givenEntity_whenConvert_thenCorrectDto() {
        Invoice invoice = getSampleEntity();
        InvoiceDto convertedDto = mapper.convertToDto(invoice);

        assertAll(() -> {
            assertEquals("F/16", convertedDto.getObjectName());
            assertEquals(CUSTOMER_ID, convertedDto.getSeller());
            assertEquals(CUSTOMER_ID, convertedDto.getBuyer());
            assertEquals("2011-11-10", convertedDto.getIssueDate());
            assertEquals("2011-11-10", convertedDto.getServiceDate());
            assertEquals("Test comment", convertedDto.getComments());
        });
    }

    @Test
    void givenDto_whenConvert_thenCorrectEntity() {
        InvoiceDto invoiceDto = getSampleDto();
        Customer customer = mockAndGetCustomer();
        Invoice convertedEntity = mapper.convertToEntity(invoiceDto);

        assertAll(() -> {
            assertEquals("F/32", convertedEntity.getObjectName());
            assertEquals(customer, convertedEntity.getSeller());
            assertEquals(customer, convertedEntity.getBuyer());
            assertEquals(LocalDate.of(2011, 11, 11), convertedEntity.getIssueDate());
            assertEquals(LocalDate.of(2011, 11, 11), convertedEntity.getServiceDate());
            assertEquals("Test DTO comment", convertedEntity.getComments());
        });
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        Invoice invoice = getSampleEntity();
        InvoiceDto invoiceDto = getSampleDto();
        Customer customer = mockAndGetCustomer();

        mapper.updateEntityFromDto(invoiceDto, invoice);

        assertAll(() -> {
            assertEquals("F/32", invoice.getObjectName());
            assertEquals(customer, invoice.getSeller());
            assertEquals(customer, invoice.getBuyer());
            assertEquals(LocalDate.of(2011, 11, 11), invoice.getIssueDate());
            assertEquals(LocalDate.of(2011, 11, 11), invoice.getServiceDate());
            assertEquals("Test DTO comment", invoice.getComments());
        });
    }

    @Test
    void givenDtoAndEmptyEntity_whenUpdate_thenEntityHasDtoProperties() {
        Invoice invoice = new Invoice();
        InvoiceDto invoiceDto = getSampleDto();
        Customer customer = mockAndGetCustomer();

        mapper.updateEntityFromDto(invoiceDto, invoice);

        assertAll(() -> {
            assertEquals("F/32", invoice.getObjectName());
            assertEquals(customer, invoice.getSeller());
            assertEquals(customer, invoice.getBuyer());
            assertEquals(LocalDate.of(2011, 11, 11), invoice.getIssueDate());
            assertEquals(LocalDate.of(2011, 11, 11), invoice.getServiceDate());
            assertEquals("Test DTO comment", invoice.getComments());
        });
    }

    @Test
    void givenNullDto_whenUpdate_thenDoNothing() {
        Invoice invoice = getSampleEntity();
        Customer customer = mockAndGetCustomer();

        mapper.updateEntityFromDto(null, invoice);

        assertAll(() -> {
            assertEquals("F/16", invoice.getObjectName());
            assertEquals(customer, invoice.getSeller());
            assertEquals(customer, invoice.getBuyer());
            assertEquals(LocalDate.of(2011, 11, 10), invoice.getIssueDate());
            assertEquals(LocalDate.of(2011, 11, 10), invoice.getServiceDate());
            assertEquals("Test comment", invoice.getComments());
        });
    }

    private Invoice getSampleEntity() {
        Customer customer = new Customer();
        customer.setId(UUID.fromString(CUSTOMER_ID));

        Invoice invoice = new Invoice();
        invoice.setObjectName("F/16");
        invoice.setSeller(customer);
        invoice.setBuyer(customer);
        invoice.setIssueDate(LocalDate.of(2011, 11, 10));
        invoice.setServiceDate(LocalDate.of(2011, 11, 10));
        invoice.setComments("Test comment");

        Payment payment = new Payment();
        payment.setBankAccount(getBankAccount());
        invoice.setPayment(payment);

        return invoice;
    }

    private BankAccount getBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.fromString(ACCOUNT_ID));
        bankAccount.setAccountNumber(RandomStringUtils.randomAlphanumeric(24));
        bankAccount.setBankName("Test bank");

        return bankAccount;
    }

    private static InvoiceDto getSampleDto() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setObjectName("F/32");
        invoiceDto.setSeller(CUSTOMER_ID);
        invoiceDto.setBuyer(CUSTOMER_ID);
        invoiceDto.setIssueDate("2011-11-11");
        invoiceDto.setServiceDate("2011-11-11");
        invoiceDto.setComments("Test DTO comment");

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setBankAccount(ACCOUNT_ID);
        invoiceDto.setPayment(paymentDto);

        return invoiceDto;
    }

    private Customer mockAndGetCustomer() {
        Customer customer = new Customer();
        customer.setId(UUID.fromString(CUSTOMER_ID));
        when(customerRepository.getReferenceById(any(UUID.class))).thenReturn(customer);

        return customer;
    }
}