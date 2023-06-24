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

import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.bankaccount.BankAccountDto;
import com.avispa.microf.model.bankaccount.BankAccountMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class BankAccountMapperIntegrationTest {
    private final BankAccountMapper mapper = Mappers.getMapper(BankAccountMapper.class);

    @Test
    void givenEntityToDto_whenMaps_thenCorrect() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setObjectName(RandomStringUtils.randomAlphabetic(40));

        BankAccountDto bankAccountDto = mapper.convertToDto(bankAccount);

        assertEquals(bankAccount.getObjectName(), bankAccountDto.getObjectName());
    }

    @Test
    void givenDtoToEntity_whenMaps_thenCorrect() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setObjectName(RandomStringUtils.randomAlphabetic(40));

        BankAccount bankAccount = mapper.convertToEntity(bankAccountDto);

        assertEquals(bankAccountDto.getObjectName(), bankAccount.getObjectName());
    }

    @Test
    void givenAccountNumberFromEntityToDto_whenMaps_thenCorrectFormat() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("PL123456789012345678");

        BankAccountDto bankAccountDto = mapper.convertToDto(bankAccount);

        assertEquals("PL 12 3456 7890 1234 5678", bankAccountDto.getAccountNumber());
    }

    @Test
    void givenAccountNumberFromDtoToEntity_whenMaps_thenCorrectFormat() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setAccountNumber("PL 12 3456 7890 1234 5678");

        BankAccount bankAccount = mapper.convertToEntity(bankAccountDto);

        assertEquals("PL123456789012345678", bankAccount.getAccountNumber());
    }
}