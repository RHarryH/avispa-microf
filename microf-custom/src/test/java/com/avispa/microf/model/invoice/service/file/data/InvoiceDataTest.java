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

package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.position.Position;
import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.address.Address;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.service.file.data.InvoiceData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class InvoiceDataTest {
    @Mock
    private Dictionary unitDict;

    @Mock
    private Dictionary vatRateDict;

    @Mock
    private Dictionary paymentTypeDict;

    @Test
    void convertToDetails() {
        when(unitDict.isEmpty()).thenReturn(false);
        when(unitDict.getLabel(anyString())).thenReturn("godz.");

        when(vatRateDict.getLabel(anyString())).thenReturn("5%");
        when(vatRateDict.getColumnValue(anyString(), anyString())).thenReturn("0.05");

        when(paymentTypeDict.isEmpty()).thenReturn(false);
        when(paymentTypeDict.getLabel(anyString())).thenReturn("przelew bankowy");

        Address address = new Address();
        address.setObjectName("A");
        address.setPlace("Kielce");
        address.setStreet("Strit");
        address.setZipCode("123");

        Customer customer = new Customer();
        customer.setType("RETAIL");
        customer.setObjectName("Name Second name");
        customer.setFirstName("Name");
        customer.setLastName("Second name");
        customer.setEmail("a@a.pl");
        customer.setPhoneNumber("+48 123 123 123");
        customer.setAddress(address);

        Position position = new Position();
        position.setPositionName("Position");
        position.setQuantity(BigDecimal.ONE);
        position.setUnit("HOUR");
        position.setVatRate("VAT_05");

        position.setUnitPrice(BigDecimal.TEN);
        position.setDiscount(BigDecimal.ZERO);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.randomUUID());
        bankAccount.setAccountNumber(RandomStringUtils.randomAlphanumeric(24));
        bankAccount.setBankName("test bank");

        Payment payment = new Payment();
        payment.setMethod("BANK_TRANSFER");
        payment.setDeadline(14);
        payment.setPaidAmount(BigDecimal.ZERO);
        payment.setBankAccount(bankAccount);

        Invoice invoice = new Invoice();
        invoice.setObjectName("123");
        invoice.setBuyer(customer);
        invoice.setSeller(customer);
        invoice.setIssueDate(LocalDate.of(2022, Month.JULY, 17));
        invoice.setServiceDate(LocalDate.now());
        invoice.setComments("Comment");
        invoice.setPositions(List.of(position));
        invoice.setPayment(payment);

        InvoiceData invoiceData = new InvoiceData(invoice, unitDict, vatRateDict, paymentTypeDict);

        assertEquals("123", invoiceData.getInvoiceName());
        assertEquals("Position", invoiceData.getPositions().get(0).getPositionName());
        assertEquals("2022-07-31", invoiceData.getPayment().getDeadlineDate());
    }
}