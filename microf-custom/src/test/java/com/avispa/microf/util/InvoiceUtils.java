/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.util;

import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.address.Address;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.position.Position;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoice;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public class InvoiceUtils {
    public static CorrectionInvoice getCorrectionInvoice() {
        Position originalPosition = getPosition1();
        Position correctedPosition = getCorrectedPosition1();
        return getCorrectionInvoice(List.of(originalPosition), List.of(correctedPosition));
    }

    public static CorrectionInvoice getCorrectionInvoice(List<Position> originalPositions, List<Position> correctedPosition) {
        Payment payment = getPayment(BigDecimal.ONE, LocalDate.of(2022, Month.JULY, 19));

        Invoice invoice = getInvoice(originalPositions);

        CorrectionInvoice correctionInvoice = new CorrectionInvoice();
        correctionInvoice.setObjectName("Corrected 123");
        correctionInvoice.setIssueDate(LocalDate.of(2022, Month.JULY, 20));
        correctionInvoice.setComments("New comment");
        correctionInvoice.setCorrectionReason("Test");
        correctionInvoice.setPositions(correctedPosition);
        correctionInvoice.setPayment(payment);
        correctionInvoice.setOriginalInvoice(invoice);

        return correctionInvoice;
    }

    public static Invoice getInvoice() {
        Position position = getPosition1();
        return getInvoice(List.of(position));
    }

    public static Invoice getInvoice(List<Position> positions) {
        Customer customer = getCustomer();
        Payment payment = getPayment();

        Invoice invoice = new Invoice();
        invoice.setObjectName("123");
        invoice.setBuyer(customer);
        invoice.setSeller(customer);
        invoice.setIssueDate(LocalDate.of(2022, Month.JULY, 17));
        invoice.setServiceDate(LocalDate.now());
        invoice.setComments("Comment");
        invoice.setPositions(positions);
        invoice.setPayment(payment);

        return invoice;
    }

    private static Customer getCustomer() {
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

        return customer;
    }

    private static BankAccount getBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.randomUUID());
        bankAccount.setAccountNumber(RandomStringUtils.randomAlphanumeric(24));
        bankAccount.setBankName("test bank");

        return bankAccount;
    }

    public static Position getPosition1() {
        Position position = new Position();
        position.setPositionName("Position");
        position.setQuantity(BigDecimal.ONE);
        position.setUnit("HOUR");
        position.setVatRate("VAT_05");

        position.setUnitPrice(BigDecimal.TEN);
        position.setDiscount(BigDecimal.ZERO);

        return position;
    }

    public static Position getPosition2(String vatRate) {
        Position position = new Position();
        position.setPositionName("Another position");
        position.setQuantity(BigDecimal.TEN);
        position.setUnit("HOUR");
        position.setVatRate(vatRate);

        position.setUnitPrice(BigDecimal.TEN);
        position.setDiscount(BigDecimal.ZERO);

        return position;
    }

    public static Position getCorrectedPosition1() {
        Position position = new Position();
        position.setPositionName("Corrected position");
        position.setQuantity(new BigDecimal("2"));
        position.setUnit("HOUR");
        position.setVatRate("VAT_05");

        position.setUnitPrice(BigDecimal.ONE);
        position.setDiscount(BigDecimal.ONE);

        return position;
    }

    public static Position getCorrectedPosition2(String vatRate) {
        Position position = new Position();
        position.setPositionName("Another corrected position");
        position.setQuantity(new BigDecimal("2"));
        position.setUnit("HOUR");
        position.setVatRate(vatRate);

        position.setUnitPrice(new BigDecimal("5"));
        position.setDiscount(BigDecimal.ZERO);

        return position;
    }

    private static Payment getPayment() {
        return getPayment(BigDecimal.ZERO, LocalDate.of(2022, Month.JUNE, 17));
    }

    private static Payment getPayment(BigDecimal paidAmount, LocalDate paidAmountDate) {
        BankAccount bankAccount = getBankAccount();

        Payment payment = new Payment();
        payment.setMethod("BANK_TRANSFER");
        payment.setDeadline(14);
        payment.setPaidAmount(paidAmount);
        payment.setPaidAmountDate(paidAmountDate);
        payment.setBankAccount(bankAccount);

        return payment;
    }
}
