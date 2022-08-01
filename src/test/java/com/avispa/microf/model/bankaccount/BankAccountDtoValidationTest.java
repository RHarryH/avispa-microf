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
        bankAccountDto.setAccountNumber(RandomStringUtils.randomAlphabetic(26));
        validate(bankAccountDto, Set.of("IBAN is invalid", "The number is not a valid NRB number"));
    }

    @Test
    void givenInvalidNRB_whenValidate_thenFail() {
        bankAccountDto.setAccountNumber("PL02114020050000300201355387");
        validate(bankAccountDto, "The number is not a valid NRB number");
    }
}