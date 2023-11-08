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

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class BankAccountDtoValidationTest {
    private BankAccountDto bankAccountDto;

    @BeforeEach
    void createDto() {
        bankAccountDto = new BankAccountDto();
        bankAccountDto.setObjectName(RandomStringUtils.randomAlphabetic(30));
        bankAccountDto.setAccountNumber("PL27114020040000300201355387");
        bankAccountDto.setBankName(RandomStringUtils.randomAlphabetic(30));
    }

    @Test
    void givenAccountNameExceedMaxLength_whenValidate_thenFail() {
        bankAccountDto.setObjectName(RandomStringUtils.randomAlphabetic(51));
        validate(bankAccountDto, BankAccountDto.VM_ACCOUNT_NAME_NO_LONGER);
    }

    @Test
    void givenBankNameExceedMaxLength_whenValidate_thenFail() {
        bankAccountDto.setBankName(RandomStringUtils.randomAlphabetic(51));
        validate(bankAccountDto, BankAccountDto.VM_BANK_NAME_NO_LONGER);
    }

    @Test
    void givenInvalidIBAN_whenValidate_thenFail() {
        bankAccountDto.setAccountNumber("021140200ABCD0300201355387");
        validate(bankAccountDto, Set.of("IBAN is invalid", "The number is not a valid NRB number"));
    }

    @Test
    void givenInvalidNRB_whenValidate_thenFail() {
        bankAccountDto.setAccountNumber("PL02114020050000300201355387");
        validate(bankAccountDto, "The number is not a valid NRB number");
    }
}