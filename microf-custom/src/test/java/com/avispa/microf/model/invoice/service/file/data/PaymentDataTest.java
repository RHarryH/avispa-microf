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

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class PaymentDataTest {
    private static final String BANK_ACCOUNT = "PL27 1140 2004 0000 3002 0135 5387";
    private static final String BANK_NAME = "Test Bank";
    private static final String BANK_TRANSFER_LABEL = "przelew bankowy";
    private static final String CASH_LABEL = "gotówka";
    public static final String PAYMENT_DATE = "2022-01-15";
    public static final String PAID_AMOUNT_DATE = "2021-12-31";

    @Mock
    private Invoice invoice;

    @Mock
    private BankAccount bankAccount;

    @Mock
    private Dictionary paymentTypeDict;

    @BeforeEach
    void setMocks() {
        lenient().when(invoice.getIssueDate()).thenReturn(LocalDate.of(2022, Month.JANUARY, 1));

        lenient().when(bankAccount.getFormattedAccountNumber()).thenReturn(BANK_ACCOUNT);
        lenient().when(bankAccount.getBankName()).thenReturn(BANK_NAME);

        lenient().when(paymentTypeDict.isEmpty()).thenReturn(false);
        lenient().when(paymentTypeDict.getLabel("BANK_TRANSFER")).thenReturn(BANK_TRANSFER_LABEL);
        lenient().when(paymentTypeDict.getLabel("CASH")).thenReturn(CASH_LABEL);
    }

    @Test
    void givenPaidAmountEqualGrossValueAndCashMethod_whenConvertToData_thenMethodAndPaidAmountDateAreSpecified() {
        Payment payment = new Payment();
        payment.setMethod("CASH");
        payment.setPaidAmount(BigDecimal.TEN);
        payment.setPaidAmountDate(LocalDate.of(2021, Month.DECEMBER, 31));
        payment.setBankAccount(bankAccount);

        when(invoice.getPayment()).thenReturn(payment);

        PaymentData paymentData = PaymentData.of(invoice, BigDecimal.TEN, paymentTypeDict);

        assertEquals("DO ZAPŁATY", paymentData.getStatus());
        assertEquals(CASH_LABEL, paymentData.getMethod());
        assertEquals(PAID_AMOUNT_DATE, paymentData.getPaidAmountDate());
        assertEquals("-", paymentData.getDeadlineDate());
        assertEquals(BigDecimal.ZERO, paymentData.getAmount());
        assertEquals("-", paymentData.getBankName());
        assertEquals("-", paymentData.getBankAccountNumber());
    }

    @Test
    void givenPaidAmountEqualGrossValueAndNonCashMethod_whenConvertToData_thenOnlyMethodIsSpecified() {
        Payment payment = new Payment();
        payment.setMethod("BANK_TRANSFER");
        payment.setPaidAmount(BigDecimal.TEN);
        payment.setPaidAmountDate(LocalDate.of(2021, Month.DECEMBER, 31));
        payment.setBankAccount(bankAccount);

        when(invoice.getPayment()).thenReturn(payment);

        PaymentData paymentData = PaymentData.of(invoice, BigDecimal.TEN, paymentTypeDict);

        assertEquals("DO ZAPŁATY", paymentData.getStatus());
        assertEquals(BANK_TRANSFER_LABEL, paymentData.getMethod());
        assertEquals(PAID_AMOUNT_DATE, paymentData.getPaidAmountDate());
        assertEquals("-", paymentData.getDeadlineDate());
        assertEquals(BigDecimal.ZERO, paymentData.getAmount());
        assertEquals("-", paymentData.getBankName());
        assertEquals("-", paymentData.getBankAccountNumber());
    }

    @Test
    void givenPaidAmountNothingAndCashMethod_whenConvertToData_thenMethodAndAmountAreSpecified() {
        Payment payment = new Payment();
        payment.setMethod("CASH");
        payment.setDeadline(14);
        payment.setPaidAmount(BigDecimal.ZERO);
        payment.setBankAccount(bankAccount);

        when(invoice.getPayment()).thenReturn(payment);

        PaymentData paymentData = PaymentData.of(invoice, BigDecimal.TEN, paymentTypeDict);

        assertEquals("DO ZAPŁATY", paymentData.getStatus());
        assertEquals(CASH_LABEL, paymentData.getMethod());
        assertEquals("-", paymentData.getPaidAmountDate());
        assertEquals(PAYMENT_DATE, paymentData.getDeadlineDate());
        assertEquals(BigDecimal.TEN, paymentData.getAmount());
        assertEquals("-", paymentData.getBankName());
        assertEquals("-", paymentData.getBankAccountNumber());
    }

    @Test
    void givenPaidAmountNothingAndNonCashMethod_whenConvertToData_thenBankDetailsAndDateAreSpecified() {
        Payment payment = new Payment();
        payment.setMethod("BANK_TRANSFER");
        payment.setDeadline(14);
        payment.setPaidAmount(BigDecimal.ZERO);
        payment.setBankAccount(bankAccount);

        when(invoice.getPayment()).thenReturn(payment);

        PaymentData paymentData = PaymentData.of(invoice, BigDecimal.TEN, paymentTypeDict);

        assertEquals("DO ZAPŁATY", paymentData.getStatus());
        assertEquals(BANK_TRANSFER_LABEL, paymentData.getMethod());
        assertEquals("-", paymentData.getPaidAmountDate());
        assertEquals(PAYMENT_DATE, paymentData.getDeadlineDate());
        assertEquals(BigDecimal.TEN, paymentData.getAmount());
        assertEquals(BANK_NAME, paymentData.getBankName());
        assertEquals(BANK_ACCOUNT, paymentData.getBankAccountNumber());
    }

    @Test
    void givenPaidAmountLessThanGrossValueAndCashMethod_whenConvertToData_thenCorrectPayAmountIsSpecified() {
        Payment payment = new Payment();
        payment.setMethod("CASH");
        payment.setDeadline(14);
        payment.setPaidAmount(BigDecimal.ONE);
        payment.setPaidAmountDate(LocalDate.of(2021, Month.DECEMBER, 31));
        payment.setBankAccount(bankAccount);

        when(invoice.getPayment()).thenReturn(payment);

        PaymentData paymentData = PaymentData.of(invoice, BigDecimal.TEN, paymentTypeDict);

        assertEquals("DO ZAPŁATY", paymentData.getStatus());
        assertEquals(CASH_LABEL, paymentData.getMethod());
        assertEquals(PAID_AMOUNT_DATE, paymentData.getPaidAmountDate());
        assertEquals(PAYMENT_DATE, paymentData.getDeadlineDate());
        assertEquals(BigDecimal.valueOf(9), paymentData.getAmount());
        assertEquals("-", paymentData.getBankName());
        assertEquals("-", paymentData.getBankAccountNumber());
    }

    @Test
    void givenPaidAmountMoreThanGrossValueAndCashMethodAndExcessPayment_whenConvertToData_thenCorrectPayAmountAndStatusAreSpecified() {
        Payment payment = new Payment();
        payment.setMethod("CASH");
        payment.setDeadline(14);
        payment.setPaidAmount(BigDecimal.valueOf(11));
        payment.setPaidAmountDate(LocalDate.of(2021, Month.DECEMBER, 31));
        payment.setBankAccount(bankAccount);

        when(invoice.getPayment()).thenReturn(payment);

        PaymentData paymentData = PaymentData.of(invoice, BigDecimal.TEN, paymentTypeDict);

        assertEquals("NADPŁATA", paymentData.getStatus());
        assertEquals(CASH_LABEL, paymentData.getMethod());
        assertEquals(PAID_AMOUNT_DATE, paymentData.getPaidAmountDate());
        assertEquals("-", paymentData.getDeadlineDate());
        assertEquals(BigDecimal.valueOf(-1), paymentData.getAmount());
        assertEquals("-", paymentData.getBankName());
        assertEquals("-", paymentData.getBankAccountNumber());
    }

    @Test
    void givenPaidNothingAndNonCashMethod_whenConvertToData_thenBankDetailsAndDeadlineAreSpecified() {
        Payment payment = new Payment();
        payment.setMethod("BANK_TRANSFER");
        payment.setDeadline(14);
        payment.setPaidAmount(BigDecimal.ONE);
        payment.setPaidAmountDate(LocalDate.of(2021, Month.DECEMBER, 31));
        payment.setBankAccount(bankAccount);

        when(invoice.getPayment()).thenReturn(payment);

        PaymentData paymentData = PaymentData.of(invoice, BigDecimal.TEN, paymentTypeDict);

        assertEquals("DO ZAPŁATY", paymentData.getStatus());
        assertEquals(BANK_TRANSFER_LABEL, paymentData.getMethod());
        assertEquals(PAID_AMOUNT_DATE, paymentData.getPaidAmountDate());
        assertEquals(PAYMENT_DATE, paymentData.getDeadlineDate());
        assertEquals(BigDecimal.valueOf(9), paymentData.getAmount());
        assertEquals(BANK_NAME, paymentData.getBankName());
        assertEquals(BANK_ACCOUNT, paymentData.getBankAccountNumber());
    }
}