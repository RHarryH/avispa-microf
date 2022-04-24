package com.avispa.microf.model.bankaccount;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class BankAccountDtoValidationTest {
    private BankAccountDto bankAccountDto;

    @BeforeEach
    void createDto() {
        bankAccountDto = new BankAccountDto();
        bankAccountDto.setAccountName(RandomStringUtils.randomAlphabetic(30));
        bankAccountDto.setBankName(RandomStringUtils.randomAlphabetic(30));
    }

    @Test
    void givenAccountNameExceedMaxLength_whenValidate_thenFail() {
        bankAccountDto.setAccountName(RandomStringUtils.randomAlphabetic(51));
        validate(bankAccountDto, BankAccountDto.VM_ACCOUNT_NAME_NO_LONGER);
    }

    @Test
    void givenBankNameExceedMaxLength_whenValidate_thenFail() {
        bankAccountDto.setBankName(RandomStringUtils.randomAlphabetic(51));
        validate(bankAccountDto, BankAccountDto.VM_BANK_NAME_NO_LONGER);
    }
}