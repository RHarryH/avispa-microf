package com.avispa.microf.model.invoice;

import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.invoice.payment.PaymentDto;
import com.avispa.microf.model.invoice.position.PositionDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static com.avispa.microf.util.TestValidationUtils.getCurrentDate;
import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class InvoiceDtoValidationTest {
    private InvoiceDto invoiceDto;

    @BeforeEach
    void createDto() {
        invoiceDto = new InvoiceDto();
        invoiceDto.setBuyer(UUID.randomUUID().toString());
        invoiceDto.setSeller(UUID.randomUUID().toString());
        invoiceDto.setServiceDate(getCurrentDate());
        invoiceDto.setIssueDate(getCurrentDate());

        PositionDto positionDto = invoiceDto.getPositions().get(0); // first position is always available when created
        positionDto.setObjectName("Name");
        positionDto.setUnit("UNIT");
        positionDto.setVatRate("RATE");
        positionDto.setUnitPrice(BigDecimal.ONE);

        PaymentDto paymentDto = invoiceDto.getPayment();
        paymentDto.setMethod("BANK_TRANSFER");
        paymentDto.setPaidAmount(BigDecimal.ZERO);
        paymentDto.setDeadline(14);
        paymentDto.setBankAccount(RandomStringUtils.randomAlphanumeric(20));
    }

    @Test
    void givenNullBuyer_whenValidate_thenFail() {
        invoiceDto.setBuyer(null);
        validate(invoiceDto, InvoiceDto.VM_BUYER_NOT_NULL);
    }

    @Test
    void givenNullSeller_whenValidate_thenFail() {
        invoiceDto.setSeller(null);
        validate(invoiceDto, InvoiceDto.VM_SELLER_NOT_NULL);
    }

    @Test
    void givenEmptyPositions_whenValidate_thenFail() {
        invoiceDto.setPositions(Collections.emptyList());
        validate(invoiceDto, InvoiceDto.VM_POSITIONS_NOT_EMPTY);
    }

    @Test
    void givenCommentsExceedMaxLength_whenValidate_thenFail() {
        invoiceDto.setComments(RandomStringUtils.randomAlphabetic(201));
        validate(invoiceDto, InvoiceDto.VM_COMMENTS_NO_LONGER);
    }
}