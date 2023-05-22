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