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

import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.bankaccount.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
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
@ContextConfiguration(classes = {PaymentMapperImpl.class})
class PaymentMapperTest {
    public static final String ACCOUNT_ID = "97ddd2fc-6cd4-4bfa-86bc-93d95e0a3a88";

    @Autowired
    private PaymentMapper mapper;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Test
    void givenEntity_whenConvert_thenCorrectDto() {
        Payment payment = getSampleEntity();
        PaymentDto convertedDto = mapper.convertToDto(payment);

        assertAll(() -> {
            assertEquals("BANK_TRANSFER", convertedDto.getMethod());
            assertEquals(BigDecimal.ONE, convertedDto.getPaidAmount());
            assertEquals("2022-11-12", convertedDto.getPaidAmountDate());
            assertEquals(10, convertedDto.getDeadline());
            assertEquals(ACCOUNT_ID, convertedDto.getBankAccount());
        });
    }

    @Test
    void givenDto_whenConvert_thenCorrectEntity() {
        PaymentDto paymentDto = getSampleDto();
        BankAccount bankAccount = mockAndGetBankAccount();
        Payment convertedEntity = mapper.convertToEntity(paymentDto);

        assertAll(() -> {
            assertEquals("CASH", convertedEntity.getMethod());
            assertEquals(BigDecimal.TEN, convertedEntity.getPaidAmount());
            assertEquals(LocalDate.of(2022, 11, 11), convertedEntity.getPaidAmountDate());
            assertEquals(15, convertedEntity.getDeadline());
            assertEquals(bankAccount, convertedEntity.getBankAccount());
        });
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        Payment payment = getSampleEntity();
        PaymentDto paymentDto = getSampleDto();
        BankAccount bankAccount = mockAndGetBankAccount();

        mapper.updateEntityFromDto(paymentDto, payment);

        assertAll(() -> {
            assertEquals("Payment entity", payment.getObjectName());
            assertEquals("CASH", payment.getMethod());
            assertEquals(BigDecimal.TEN, payment.getPaidAmount());
            assertEquals(LocalDate.of(2022, 11, 11), payment.getPaidAmountDate());
            assertEquals(15, payment.getDeadline());
            assertEquals(bankAccount, payment.getBankAccount());
        });
    }

    @Test
    void givenDtoAndEmptyEntity_whenUpdate_thenEntityHasDtoProperties() {
        Payment payment = new Payment();
        PaymentDto paymentDto = getSampleDto();
        BankAccount bankAccount = mockAndGetBankAccount();

        mapper.updateEntityFromDto(paymentDto, payment);

        assertAll(() -> {
            assertEquals("CASH", payment.getMethod());
            assertEquals(BigDecimal.TEN, payment.getPaidAmount());
            assertEquals(LocalDate.of(2022, 11, 11), payment.getPaidAmountDate());
            assertEquals(15, payment.getDeadline());
            assertEquals(bankAccount, payment.getBankAccount());
        });
    }

    @Test
    void givenNullDto_whenUpdate_thenDoNothing() {
        Payment payment = getSampleEntity();
        mapper.updateEntityFromDto(null, payment);

        assertAll(() -> {
            assertEquals("Payment entity", payment.getObjectName());
            assertEquals("BANK_TRANSFER", payment.getMethod());
            assertEquals(BigDecimal.ONE, payment.getPaidAmount());
            assertEquals(LocalDate.of(2022, 11, 12), payment.getPaidAmountDate());
            assertEquals(10, payment.getDeadline());
            assertEquals(ACCOUNT_ID, payment.getBankAccount().getId().toString());
        });
    }

    private Payment getSampleEntity() {
        Payment payment = new Payment();
        payment.setObjectName("Payment entity");
        payment.setMethod("BANK_TRANSFER");
        payment.setPaidAmount(BigDecimal.ONE);
        payment.setPaidAmountDate(LocalDate.of(2022, 11, 12));
        payment.setDeadline(10);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.fromString(ACCOUNT_ID));
        payment.setBankAccount(bankAccount);

        return payment;
    }

    private PaymentDto getSampleDto() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setMethod("CASH");
        paymentDto.setPaidAmount(BigDecimal.TEN);
        paymentDto.setPaidAmountDate("2022-11-11");
        paymentDto.setDeadline(15);
        paymentDto.setBankAccount(ACCOUNT_ID);

        return paymentDto;
    }

    private BankAccount mockAndGetBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.fromString(ACCOUNT_ID));
        when(bankAccountRepository.getReferenceById(any(UUID.class))).thenReturn(bankAccount);

        return bankAccount;
    }
}