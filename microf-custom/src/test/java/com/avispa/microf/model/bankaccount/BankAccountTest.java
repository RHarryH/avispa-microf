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