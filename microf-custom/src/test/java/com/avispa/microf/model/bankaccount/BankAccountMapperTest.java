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

package com.avispa.microf.model.bankaccount;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class BankAccountMapperTest {
    private final BankAccountMapper mapper = Mappers.getMapper(BankAccountMapper.class);

    @Test
    void givenEntity_whenConvert_thenCorrectDto() {
        BankAccount bankAccount = getSampleEntity();
        BankAccountDto convertedDto = mapper.convertToDto(bankAccount);

        assertAll(() -> {
            assertEquals("Bank entity", convertedDto.getObjectName());
            assertEquals("Bank", convertedDto.getBankName());
            assertEquals("PL 12 3456 7890 1234 5678", convertedDto.getAccountNumber());
        });
    }

    @Test
    void givenDto_whenConvert_thenCorrectEntity() {
        BankAccountDto bankAccountDto = getSampleDto();
        BankAccount convertedEntity = mapper.convertToEntity(bankAccountDto);

        assertAll(() -> {
            assertEquals("Bank DTO", convertedEntity.getObjectName());
            assertEquals("DTO Bank", convertedEntity.getBankName());
            assertEquals("PL123456789012345678", convertedEntity.getAccountNumber());
        });
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        BankAccount bankAccount = getSampleEntity();
        BankAccountDto bankAccountDto = getSampleDto();
        mapper.updateEntityFromDto(bankAccountDto, bankAccount);

        assertAll(() -> {
            assertEquals("Bank DTO", bankAccount.getObjectName());
            assertEquals("DTO Bank", bankAccount.getBankName());
            assertEquals("PL123456789012345678", bankAccount.getAccountNumber());
        });
    }

    @Test
    void givenDtoAndEmptyEntity_whenUpdate_thenEntityHasDtoProperties() {
        BankAccount bankAccount = new BankAccount();
        BankAccountDto bankAccountDto = getSampleDto();
        mapper.updateEntityFromDto(bankAccountDto, bankAccount);

        assertAll(() -> {
            assertEquals("Bank DTO", bankAccount.getObjectName());
            assertEquals("DTO Bank", bankAccount.getBankName());
            assertEquals("PL123456789012345678", bankAccount.getAccountNumber());
        });
    }

    @Test
    void givenNullDto_whenUpdate_thenDoNothing() {
        BankAccount bankAccount = getSampleEntity();
        mapper.updateEntityFromDto(null, bankAccount);

        assertAll(() -> {
            assertEquals("Bank entity", bankAccount.getObjectName());
            assertEquals("Bank", bankAccount.getBankName());
            assertEquals("PL123456789012345678", bankAccount.getAccountNumber());
        });
    }

    private static BankAccount getSampleEntity() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setObjectName("Bank entity");
        bankAccount.setBankName("Bank");
        bankAccount.setAccountNumber("PL123456789012345678");

        return bankAccount;
    }

    private BankAccountDto getSampleDto() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setObjectName("Bank DTO");
        bankAccountDto.setBankName("DTO Bank");
        bankAccountDto.setAccountNumber("PL 12 3456 7890 1234 5678");

        return bankAccountDto;
    }
}