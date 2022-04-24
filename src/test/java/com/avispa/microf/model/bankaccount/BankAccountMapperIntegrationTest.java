package com.avispa.microf.model.bankaccount;

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

        assertEquals(bankAccount.getObjectName(), bankAccountDto.getAccountName());
    }

    @Test
    void givenDtoToEntity_whenMaps_thenCorrect() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setAccountName(RandomStringUtils.randomAlphabetic(40));

        BankAccount bankAccount = mapper.convertToEntity(bankAccountDto);

        assertEquals(bankAccountDto.getAccountName(), bankAccount.getObjectName());
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