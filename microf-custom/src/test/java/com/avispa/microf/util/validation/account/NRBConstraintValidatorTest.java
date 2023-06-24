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

package com.avispa.microf.util.validation.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Rafał Hiszpański
 */
class NRBConstraintValidatorTest {
    private final NRBConstraintValidator nrbConstraintValidator = new NRBConstraintValidator();

    @Test
    void NRBAsIBANTest() {
        assertTrue(nrbConstraintValidator.isValid("PL00103019441234567890123456", null));
    }

    @Test
    void NRBAsIBANTest2() {
        assertTrue(nrbConstraintValidator.isValid("PL00116022021234567890123456", null));
    }

    @Test
    void NRBTest() {
        assertTrue(nrbConstraintValidator.isValid("00116022021234567890123456", null));
    }

    @Test
    void nullTest() {
        assertFalse(nrbConstraintValidator.isValid(null, null));
    }

    @Test
    void wrongLengthTest() {
        assertFalse(nrbConstraintValidator.isValid("123", null));
    }
}