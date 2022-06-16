package com.avispa.microf.model.bankaccount;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rafał Hiszpański
 */
class BankAccountTest {

    @Test
    void accountNumberIsMultiplierOf4() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("PL82102052260000610204177895");
        assertEquals("PL82 1020 5226 0000 6102 0417 7895", bankAccount.getFormattedAccountNumber());
    }

    @Test
    void accountNumberIsNotMultiplierOf4() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("ME25505000012345678951");
        assertEquals("ME25 5050 0001 2345 6789 51", bankAccount.getFormattedAccountNumber());
    }
}