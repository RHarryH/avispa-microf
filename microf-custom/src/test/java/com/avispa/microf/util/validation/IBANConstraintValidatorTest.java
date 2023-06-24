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

package com.avispa.microf.util.validation;

import com.avispa.microf.util.validation.account.IBANConstraintValidator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests based on official IBAN <a href="https://www.iban.com/testibans">tests</a>
 * @author Rafał Hiszpański
 */
class IBANConstraintValidatorTest {

    private static final IBANConstraintValidator ibanConstraintValidator = new IBANConstraintValidator();

    @Test
    @DisplayName("Valid length, checksum, bank code, account and structure")
    void validIBAN() {
        assertTrue(ibanConstraintValidator.isValid("GB33BUKB20201555555555", null));
    }

    @Test
    @DisplayName("Valid length, checksum, bank code, account and structure. Bank code not found (cannot be identified)")
    void validIBAN2() {
        assertTrue(ibanConstraintValidator.isValid("GB94BARC10201530093459", null));
    }

    @Test
    @DisplayName("Invalid check digits MOD-97-10 as per ISO/IEC 7064:2003")
    void invalidIBAN() {
        assertFalse(ibanConstraintValidator.isValid("GB94BARC20201530093459", null));
    }

    @Test
    @DisplayName("Does not have proper length")
    void invalidIBAN2() {
        assertFalse(ibanConstraintValidator.isValid("GB96BARC202015300934591", null));
    }

    @Test
    @Disabled("Does not pass because of Commons Validator implementation")
    @DisplayName("Invalid account number")
    void invalidIBAN3() {
        assertFalse(ibanConstraintValidator.isValid("GB02BARC20201530093451", null));
    }

    @Test
    @Disabled("Does not pass because of Commons Validator implementation")
    @DisplayName("Invalid account number")
    void invalidIBAN4() {
        assertFalse(ibanConstraintValidator.isValid("GB68CITI18500483515538", null));
    }

    @Test
    @Disabled("Does not pass because of Commons Validator implementation")
    @DisplayName("Bank Code not found and invalid account")
    void invalidIBAN5() {
        assertFalse(ibanConstraintValidator.isValid("GB24BARC20201630093459", null));
    }

    @Test
    @DisplayName("Invalid account structure")
    void invalidIBAN6() {
        assertFalse(ibanConstraintValidator.isValid("GB12BARC20201530093A59", null));
    }

    @Test
    @DisplayName("Invalid IBAN checksum structure")
    void invalidIBAN7() {
        assertFalse(ibanConstraintValidator.isValid("GB2LABBY09012857201707", null));
    }

    @Test
    @DisplayName("Invalid IBAN checksum")
    void invalidIBAN8() {
        assertFalse(ibanConstraintValidator.isValid("GB01BARC20714583608387", null));
    }

    @Test
    @DisplayName("Invalid IBAN checksum")
    void invalidIBAN9() {
        assertFalse(ibanConstraintValidator.isValid("GB00HLFX11016111455365", null));
    }

    @Test
    @DisplayName("Country does not seem to support IBAN")
    void invalidIBAN10() {
        assertFalse(ibanConstraintValidator.isValid("US64SVBKUS6S3300958879", null));
    }
}