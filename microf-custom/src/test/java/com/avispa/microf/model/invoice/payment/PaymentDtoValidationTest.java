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

package com.avispa.microf.model.invoice.payment;

import com.avispa.microf.model.invoice.payment.PaymentDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class PaymentDtoValidationTest {
    private PaymentDto paymentDto;

    @BeforeEach
    void createDto() {
        paymentDto = new PaymentDto();
        paymentDto.setMethod("BANK_TRANSFER");
        paymentDto.setPaidAmount(BigDecimal.ZERO);
        paymentDto.setPaidAmountDate("2022-01-01");
        paymentDto.setDeadline(14);
        paymentDto.setBankAccount(RandomStringUtils.randomAlphanumeric(20));
    }

    @Test
    void givenNullMethod_whenValidate_thenThrowsException() {
        paymentDto.setMethod(null);
        validate(paymentDto, Set.of(PaymentDto.VM_PAYMENT_METHOD_NOT_EMPTY, PaymentDto.VM_DEADLINE_AND_BANK_ACCOUNT_NOT_PRESENT));
    }

    @Test
    void givenEmptyMethod_whenValidate_thenFail() {
        paymentDto.setMethod("");
        validate(paymentDto, PaymentDto.VM_PAYMENT_METHOD_NOT_EMPTY);
    }

    @Test
    void givenNegativeDeadline_whenValidate_thenFail() {
        paymentDto.setDeadline(-10);
        validate(paymentDto, PaymentDto.VM_PAYMENT_DEADLINE_NOT_IN_RANGE);
    }

    @Test
    void givenDeadlineHigherThanYear_whenValidate_thenFail() {
        paymentDto.setDeadline(370);
        validate(paymentDto, PaymentDto.VM_PAYMENT_DEADLINE_NOT_IN_RANGE);
    }

    @Test
    void givenNegativePaidAmount_whenValidate_thenFail() {
        paymentDto.setPaidAmount(BigDecimal.valueOf(-1));
        validate(paymentDto, PaymentDto.VM_PAID_AMOUNT_POSITIVE_OR_ZERO);
    }

    @Test
    void givenNullPaidAmount_whenValidate_thenFail() {
        paymentDto.setPaidAmount(null);
        validate(paymentDto, PaymentDto.VM_PAID_AMOUNT_NOT_NULL);
    }

    @Test
    void givenPaidAmountOutOfRange_whenValidate_thenFail() {
        paymentDto.setPaidAmount(BigDecimal.valueOf(12345678));
        validate(paymentDto, PaymentDto.VM_PAID_AMOUNT_IN_RANGE);
    }

    @Test
    void givenPaidAmountGreaterThanZeroAndNullDateOfPaid_whenValidate_thenFail() {
        paymentDto.setPaidAmount(BigDecimal.ONE);
        paymentDto.setPaidAmountDate(null);
        validate(paymentDto, PaymentDto.VM_DATE_NOT_EMPTY);
    }
}